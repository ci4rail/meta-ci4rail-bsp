# SPDX-License-Identifier: MIT
# Collect pinned sources and legal evidence for downloaded Go executables.
# Source archives must be explicit, checksum-pinned SRC_URI entries.
inherit deploy
GO_CLEARING_SOURCE_DIR ??= ""
GO_CLEARING_BINARIES ??= ""
GO_CLEARING_GO_VERSION ??= ""
GO_CLEARING_GO_SOURCE_URL ??= "https://go.dev/dl/${GO_CLEARING_GO_VERSION}.src.tar.gz"
GO_CLEARING_GO_SHA256 ??= ""
GO_CLEARING_PROXY ??= "https://proxy.golang.org"
GO_CLEARING_EXTRA_SUMS ??= ""
GO_CLEARING_WORK = "${WORKDIR}/go-prebuilt-clearing"

python do_fetch_go_clearing() {
    import os
    import hashlib
    import go_prebuilt_clearing

    def fetch(url, checksum):
        local = d.createCopy()
        # Modules are verified using Go's h1 directory hash after fetching.
        local.setVar('BB_STRICT_CHECKSUM', '1' if checksum else 'ignore')
        local.delVarFlags('SRC_URI')
        filename = 'go-clearing-' + hashlib.sha256(url.encode()).hexdigest()
        uri = url + ';downloadfilename=' + filename
        local.setVar('SRC_URI', uri)
        if checksum:
            local.setVarFlag('SRC_URI', 'sha256sum', checksum)
        fetcher = bb.fetch2.Fetch([uri], local)
        fetcher.download()
        return fetcher.localpath(uri)

    try:
        report = go_prebuilt_clearing.collect(
            d.getVar('GO_CLEARING_SOURCE_DIR'), d.getVar('GO_CLEARING_BINARIES').split(),
            os.path.join(d.getVar('STAGING_BINDIR_NATIVE'), 'go'),
            d.getVar('GO_CLEARING_GO_VERSION'), d.getVar('GO_CLEARING_GO_SHA256'),
            d.getVar('GO_CLEARING_PROXY'), d.getVar('GO_CLEARING_WORK'), fetch,
            toolchain_url=d.getVar('GO_CLEARING_GO_SOURCE_URL'),
            extra_sums=d.getVar('GO_CLEARING_EXTRA_SUMS') or None)
    except (ValueError, OSError) as error:
        bb.fatal(str(error))
    for issue in report['unresolved']:
        bb.warn('Go clearing: ' + issue)
}
do_fetch_go_clearing[network] = "1"
do_fetch_go_clearing[cleandirs] = "${GO_CLEARING_WORK}"
do_fetch_go_clearing[file-checksums] += "${GO_CLEARING_HELPER}:True"
do_fetch_go_clearing[depends] += "go-native:do_populate_sysroot"
addtask fetch_go_clearing after do_unpack do_prepare_recipe_sysroot before do_populate_lic do_deploy

# Append before license.bbclass stages its output in sstate.
python do_populate_lic:append() {
    # Appends share the base task's scope; avoid shadowing its global imports.
    import os as clearing_os
    import shutil as clearing_shutil
    destination = clearing_os.path.join(d.getVar('LICSSTATEDIR'),
                               d.getVar('LICENSE_DEPLOY_PATHCOMPONENT'), d.getVar('PN'))
    clearing_shutil.copytree(clearing_os.path.join(d.getVar('GO_CLEARING_WORK'), 'legal'),
                    destination, dirs_exist_ok=True)
}

python do_deploy() {
    import os
    import shutil
    shutil.copytree(os.path.join(d.getVar('GO_CLEARING_WORK'), 'sources'),
                    os.path.join(d.getVar('DEPLOYDIR'), d.getVar('PN') + '-' + d.getVar('PV')))
}
# Use the standard recursively scheduled deployment task, but stage sources.
do_deploy[sstate-outputdirs] = "${DEPLOY_DIR}/sources"
addtask deploy after do_fetch_go_clearing before do_build do_rm_work
