import base64
import hashlib
import io
import os
from pathlib import Path
import sys
import tempfile
import tarfile
import unittest
from unittest.mock import patch
import zipfile

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'lib'))
from go_prebuilt_clearing import binary_metadata, collect, module_escape, verify_module


class ClearingTests(unittest.TestCase):
    def test_license_append_shares_base_task_scope(self):
        layer = Path(__file__).resolve().parents[1]
        text = (layer / 'classes/go-prebuilt-clearing.bbclass').read_text()
        append = text.split('python do_populate_lic:append() {', 1)[1].split('\n}', 1)[0]
        # BitBake concatenates the base task and append in a single function.
        base = '\n    destination = os.path.join(d.getVar("LICSSTATEDIR"), d.getVar("LICENSE_DEPLOY_PATHCOMPONENT"), d.getVar("PN"))\n'
        namespace = {'os': os}
        exec('def task(d):' + base + append, namespace)
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            legal = root / 'work/legal'
            legal.mkdir(parents=True)
            (legal / 'LICENSE').write_text('collected license')
            values = {'LICSSTATEDIR': str(root / 'licenses'),
                      'LICENSE_DEPLOY_PATHCOMPONENT': 'arch', 'PN': 'app',
                      'GO_CLEARING_WORK': str(root / 'work')}
            destination = root / 'licenses/arch/app'
            destination.mkdir(parents=True)
            (destination / 'recipeinfo').write_text('base task output')
            namespace['task'](type('Data', (), {'getVar': lambda _, name: values[name]})())
            self.assertEqual((destination / 'LICENSE').read_text(), 'collected license')
            self.assertEqual((destination / 'recipeinfo').read_text(), 'base task output')

    def test_missing_go_sum_only_allows_no_binary_dependencies(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            source = root / 'app'
            source.mkdir()
            (source / 'go.mod').write_text('module example.com/app\n\ngo 1.23\n')
            (source / 'LICENSE').write_text('application license')
            binary = root / 'binary'
            binary.write_bytes(b'test binary')
            runtime = root / 'runtime.tar.gz'
            with tarfile.open(runtime, 'w:gz') as archive:
                entry = tarfile.TarInfo('go/LICENSE')
                data = b'runtime license'
                entry.size = len(data)
                archive.addfile(entry, io.BytesIO(data))
            checksum = hashlib.sha256(runtime.read_bytes()).hexdigest()
            metadata = ('binary: go1.23.2\n\tmod\texample.com/app\t(devel)\n'
                        '\tbuild\tCGO_ENABLED=0\n')
            with patch('go_prebuilt_clearing.subprocess.check_output', return_value=metadata):
                report = collect(source, [str(binary)], 'go', 'go1.23.2', checksum,
                                 'https://example.test', root / 'output', lambda *_: runtime)
            self.assertEqual(report['modules'], [])
            self.assertEqual(report['go_runtime']['url'], 'https://go.dev/dl/go1.23.2.src.tar.gz')
            custom_url = 'https://example.test/go/archive/pinned-commit.tar.gz'
            calls = []
            def custom_fetch(url, sha):
                calls.append((url, sha))
                return runtime
            with patch('go_prebuilt_clearing.subprocess.check_output', return_value=metadata):
                custom = collect(source, [str(binary)], 'go', 'go1.23.2', checksum,
                                 'https://example.test', root / 'custom', custom_fetch,
                                 toolchain_url=custom_url)
                self.assertEqual(calls, [(custom_url, checksum)])
                self.assertEqual(custom['go_runtime']['url'], custom_url)
                with self.assertRaisesRegex(ValueError, 'Go source checksum mismatch'):
                    collect(source, [str(binary)], 'go', 'go1.23.2', '0' * 64,
                            'https://example.test', root / 'bad-runtime', custom_fetch,
                            toolchain_url=custom_url)
            with patch('go_prebuilt_clearing.subprocess.check_output', return_value=
                       metadata + '\tdep\texample.com/dep\tv1.0.0\th1:abc\n'):
                with self.assertRaisesRegex(ValueError, 'not verified by go.sum'):
                    collect(source, [str(binary)], 'go', 'go1.23.2', checksum,
                            'https://example.test', root / 'rejected', lambda *_: runtime)
            (source / 'go.sum').write_text('example.com/dep v1.0.0 h1:original\n')
            extra = root / 'extra.sum'
            extra.write_text('example.com/dep v1.0.0 h1:changed\n')
            with self.assertRaisesRegex(ValueError, 'Conflicting module checksum'):
                collect(source, [str(binary)], 'go', 'go1.23.2', checksum,
                        'https://example.test', root / 'conflict', lambda *_: runtime,
                        extra_sums=extra)

    def test_metadata_and_dirty_source(self):
        version, deps, settings = binary_metadata(
            'binary: go1.23.2\n\tdep\texample.com/a\tv1.0.0\th1:abc\n'
            '\tbuild\tvcs.modified=true\n\tbuild\tCGO_ENABLED=0\n')
        self.assertEqual(version, 'go1.23.2')
        self.assertEqual(deps, [('example.com/a', 'v1.0.0', 'h1:abc')])
        self.assertEqual(settings['vcs.modified'], 'true')

    def test_replacement_is_not_silently_ignored(self):
        with self.assertRaisesRegex(ValueError, 'replacements'):
            binary_metadata('binary: go1.23.2\n\t=>\t../local\t(devel)\n')

    def test_module_escape(self):
        self.assertEqual(module_escape('github.com/Azure/X'), 'github.com/!azure/!x')

    def test_tampering_and_path_traversal(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / 'module.zip'
            name = 'example.com/a@v1.0.0/LICENSE'
            data = b'license text'
            line = hashlib.sha256(data).hexdigest() + '  ' + name + '\n'
            expected = 'h1:' + base64.b64encode(hashlib.sha256(line.encode()).digest()).decode()
            with zipfile.ZipFile(path, 'w') as archive:
                archive.writestr(name, data)
            verify_module(path, 'example.com/a', 'v1.0.0', expected)
            with zipfile.ZipFile(path, 'w') as archive:
                archive.writestr(name, b'changed')
            with self.assertRaisesRegex(ValueError, 'checksum mismatch'):
                verify_module(path, 'example.com/a', 'v1.0.0', expected)
            with zipfile.ZipFile(path, 'w') as archive:
                archive.writestr('example.com/a@v1.0.0/../evil', data)
            with self.assertRaisesRegex(ValueError, 'Unsafe'):
                verify_module(path, 'example.com/a', 'v1.0.0', expected)


if __name__ == '__main__':
    unittest.main()
