FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

CI4RAIL_OTA_PROTECTION ?= "0"
CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY ?= ""
CI4RAIL_OTA_DELEGATION_PUBLIC_KEY ?= ""
CI4RAIL_OTA_PROTECTION:class-native = "0"

SRC_URI:append = "${@' file://0001-enforce-ci4rail-ota-policy.patch file://ci4rail-ota-policy.hpp file://' + d.getVar('CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY') + ' file://' + d.getVar('CI4RAIL_OTA_DELEGATION_PUBLIC_KEY') if d.getVar('CI4RAIL_OTA_PROTECTION') == '1' else ''}"

python __anonymous() {
    if d.getVar('CI4RAIL_OTA_PROTECTION') == '1':
        for name in ('CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY', 'CI4RAIL_OTA_DELEGATION_PUBLIC_KEY'):
            if not d.getVar(name):
                bb.fatal('%s must name a PEM public key for protected OTA builds' % name)
        if d.getVar('MENDER_ARTIFACT_VERIFY_KEY'):
            bb.fatal('Use CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY, not MENDER_ARTIFACT_VERIFY_KEY')
}

do_configure:prepend() {
    if [ "${CI4RAIL_OTA_PROTECTION}" = "1" ]; then
        install -m 0644 ${WORKDIR}/ci4rail-ota-policy.hpp ${S}/src/mender-update/
    fi
}

python do_prepare_mender_conf:append() {
    if d.getVar('CI4RAIL_OTA_PROTECTION') != '1':
        return
    import json
    path = os.path.join(d.getVar('B'), 'transient_mender.conf')
    with open(path) as stream:
        config = json.load(stream)
    config.pop('ArtifactVerifyKey', None)
    config['ArtifactVerifyKeys'] = [
        '/usr/share/ci4rail/ota/ci4rail-artifact-pub.pem',
        '/etc/ota/customer-artifact-pub.pem',
    ]
    with open(path, 'w') as stream:
        json.dump(config, stream, indent=4, sort_keys=True)
    persistent = os.path.join(d.getVar('B'), 'persistent_mender.conf')
    with open(persistent) as stream:
        fallback = json.load(stream)
    if any(key.lower() in ('artifactverifykey', 'artifactverifykeys') for key in fallback):
        bb.fatal('Artifact verification settings must not be persistent Mender configuration')
    for values in (config, fallback):
        for key in list(values):
            if key.lower() in ('tenanttoken', 'serverurl', 'servers', 'updatepollintervalseconds',
                               'inventorypollintervalseconds', 'retrypollintervalseconds'):
                del values[key]
    with open(os.path.join(d.getVar('B'), 'ci4rail-ota-policy.json'), 'w') as stream:
        json.dump({'main': config, 'fallback': fallback}, stream, indent=4, sort_keys=True)
}

do_install:append() {
    if [ "${CI4RAIL_OTA_PROTECTION}" = "1" ]; then
        install -d ${D}${datadir}/ci4rail/ota
        install -m 0644 ${B}/ci4rail-ota-policy.json ${D}${datadir}/ci4rail/ota/policy.json
        install -m 0644 ${CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY} ${D}${datadir}/ci4rail/ota/ci4rail-artifact-pub.pem
        install -m 0644 ${CI4RAIL_OTA_DELEGATION_PUBLIC_KEY} ${D}${datadir}/ci4rail/ota/ci4rail-delegation-pub.pem
    fi
}

FILES:mender-update:append = "${@' ${datadir}/ci4rail/ota' if d.getVar('CI4RAIL_OTA_PROTECTION') == '1' else ''}"
