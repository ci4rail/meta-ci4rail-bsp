"""Collect evidence for prebuilt Go programs; not a license compliance verdict."""

import base64
import hashlib
import json
from pathlib import Path, PurePosixPath
import re
import shutil
import subprocess
import tarfile
import zipfile


def module_escape(value):
    return ''.join('!' + c.lower() if c.isupper() else c for c in value)


def verify_module(path, module, version, expected):
    """Go dirhash.Hash1 over zip entries, including the module@version prefix."""
    with zipfile.ZipFile(path) as archive:
        names = [i.filename for i in archive.infolist() if not i.is_dir()]
        if len(names) != len(set(names)):
            raise ValueError('Duplicate module archive entries')
        digest = hashlib.sha256()
        for name in sorted(names):
            if (not name.startswith(module + '@' + version + '/')
                    or '..' in PurePosixPath(name).parts or '\n' in name
                    or '\\' in name):
                raise ValueError('Unsafe module archive path: ' + name)
            checksum = hashlib.sha256(archive.read(name)).hexdigest()
            digest.update((checksum + '  ' + name + '\n').encode())
        actual = 'h1:' + base64.b64encode(digest.digest()).decode()
        if actual != expected:
            raise ValueError('Module checksum mismatch: ' + module + '@' + version)


def binary_metadata(text):
    lines = text.splitlines()
    match = re.search(r': (go\d+\.\d+(?:\.\d+)?)$', lines[0])
    if not match:
        raise ValueError('Binary has no supported Go release metadata')
    dependencies = []
    settings = {}
    for line in lines[1:]:
        fields = line.strip().split('\t')
        if fields[0] == '=>':
            raise ValueError('Module replacements need explicit review; refusing automatic collection')
        if fields[0] == 'dep':
            if len(fields) < 4 or not fields[3].startswith('h1:'):
                raise ValueError('Binary dependency lacks a module checksum')
            dependencies.append(tuple(fields[1:4]))
        if fields[0] == 'build' and len(fields) > 1:
            key, _, value = fields[1].partition('=')
            settings[key] = value
    return match[1], dependencies, settings


def legal_text(root):
    """Preserve attribution and paths; source archives retain all file headers."""
    chunks = []
    for path in sorted(root.rglob('*')):
        if path.is_symlink() or not path.is_file():
            continue
        if re.match(r'^(licen[cs]e|copying|copyright|notice|authors|patents)([._-]|$)',
                    path.name, re.I):
            chunks.append('\n===== ' + path.relative_to(root).as_posix() + ' =====\n'
                          + path.read_text(encoding='utf-8', errors='replace'))
    return '\n'.join(chunks)


def collect(source, binaries, go, toolchain_version, toolchain_sha256, proxy,
            destination, fetch, toolchain_url=None, extra_sums=None):
    """fetch(url, sha256_or_None) returns a local file through BitBake's fetcher."""
    source, destination = Path(source), Path(destination)
    if not (source / 'go.mod').is_file():
        raise ValueError('GO_CLEARING_SOURCE_DIR must contain go.mod')
    if not re.fullmatch(r'go\d+\.\d+(?:\.\d+)?', toolchain_version):
        raise ValueError('Pin GO_CLEARING_GO_VERSION to a released Go version')
    if not re.fullmatch(r'[0-9a-f]{64}', toolchain_sha256):
        raise ValueError('Pin GO_CLEARING_GO_SHA256 to the Go source archive checksum')
    modules = {}
    sums = source / 'go.sum'
    # Standard-library-only applications legitimately have no go.sum. Any
    # binary dependency still has to match an entry below or collection fails.
    for line in (sums.read_text().splitlines() if sums.is_file() else []):
        name, version, checksum = line.split()
        if not version.endswith('/go.mod'):
            modules[name, version] = checksum
    report = {'scope': 'Application source, all go.sum module archives, and Go runtime source',
              'completeness': 'not-certified', 'unresolved': [], 'binaries': [], 'modules': []}
    if extra_sums:
        additions = Path(extra_sums).read_text()
        for line in additions.splitlines():
            if not line.strip() or line.lstrip().startswith('#'):
                continue
            name, version, checksum = line.split()
            if version.endswith('/go.mod') or not checksum.startswith('h1:'):
                raise ValueError('Extra sums must identify full module archives')
            if (name, version) in modules and modules[name, version] != checksum:
                raise ValueError('Conflicting module checksum: ' + name)
            modules[name, version] = checksum
        report['additional_module_checksums'] = additions
        report['unresolved'].append('Additional recipe-pinned module checksums supplement release go.sum')
    if re.search(r'^\s*replace\s', (source / 'go.mod').read_text(), re.M):
        raise ValueError('go.mod replacements require explicit review')
    if not binaries:
        raise ValueError('Set GO_CLEARING_BINARIES')
    for binary in binaries:
        text = subprocess.check_output([go, 'version', '-m', binary], text=True)
        source_module = re.search(r'^module\s+(\S+)', (source / 'go.mod').read_text(), re.M)
        binary_module = re.search(r'^\s*mod\t([^\t]+)', text, re.M)
        if not source_module or not binary_module or source_module[1].strip('"') != binary_module[1]:
            raise ValueError('Application module does not match binary: ' + binary)
        version, deps, settings = binary_metadata(text)
        if version != toolchain_version:
            raise ValueError('Go source version does not match binary: ' + binary)
        for name, ver, checksum in deps:
            if modules.get((name, ver)) != checksum:
                raise ValueError('Binary dependency is not verified by go.sum: ' + name)
        if settings.get('CGO_ENABLED') != '0':
            report['unresolved'].append('Cgo/native dependency review needed: ' + Path(binary).name)
        if settings.get('vcs.modified') == 'true':
            report['unresolved'].append('Binary built from modified sources: ' + Path(binary).name)
        report['binaries'].append({'name': Path(binary).name,
                                  'sha256': hashlib.sha256(Path(binary).read_bytes()).hexdigest(),
                                  'metadata': text})
    report['unresolved'].append('Release archive to binary source correspondence is not verified')
    sources = destination / 'sources'
    legal = destination / 'legal'
    sources.mkdir(parents=True)
    legal.mkdir()
    application = sources / 'application'
    shutil.copytree(source, application, symlinks=True)
    text = legal_text(application)
    if not text:
        raise ValueError('Application source contains no recognized legal files')
    (legal / 'application-legal.txt').write_text(text)
    for (name, version), checksum in sorted(modules.items()):
        url = proxy.rstrip('/') + '/' + module_escape(name) + '/@v/' + module_escape(version) + '.zip'
        path = fetch(url, None)
        verify_module(path, name, version, checksum)
        key = hashlib.sha256((name + '@' + version).encode()).hexdigest()[:20]
        shutil.copy2(path, sources / (key + '.zip'))
        with zipfile.ZipFile(path) as archive:
            notices = []
            for entry in sorted(archive.namelist()):
                if re.match(r'^(licen[cs]e|copying|copyright|notice|authors|patents)([._-]|$)',
                            PurePosixPath(entry).name, re.I) and not entry.endswith('/'):
                    notices.append('\n===== ' + entry + ' =====\n'
                                   + archive.read(entry).decode('utf-8', errors='replace'))
        if not notices:
            report['unresolved'].append('No recognized legal files: ' + name + '@' + version)
        (legal / (key + '-legal.txt')).write_text(name + '@' + version + '\n' + '\n'.join(notices))
        report['modules'].append({'module': name, 'version': version, 'h1': checksum,
                                  'archive': key + '.zip', 'url': url})
    toolchain_url = toolchain_url or 'https://go.dev/dl/' + toolchain_version + '.src.tar.gz'
    runtime = fetch(toolchain_url, toolchain_sha256)
    if hashlib.sha256(Path(runtime).read_bytes()).hexdigest() != toolchain_sha256:
        raise ValueError('Go source checksum mismatch')
    shutil.copy2(runtime, sources / (toolchain_version + '.src.tar.gz'))
    with tarfile.open(runtime) as archive:
        notices = []
        for entry in archive:
            if entry.isfile() and re.match(
                    r'^(licen[cs]e|copying|copyright|notice|authors|patents)([._-]|$)',
                    PurePosixPath(entry.name).name, re.I):
                notices.append('\n===== ' + entry.name + ' =====\n'
                               + archive.extractfile(entry).read().decode('utf-8', errors='replace'))
    (legal / 'go-runtime-legal.txt').write_text('\n'.join(notices))
    report['go_runtime'] = {'version': toolchain_version, 'sha256': toolchain_sha256,
                            'url': toolchain_url}
    for directory in (legal, sources):
        (directory / 'go-clearing-report.json').write_text(json.dumps(report, indent=2) + '\n')
    return report
