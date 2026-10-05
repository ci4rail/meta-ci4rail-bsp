# Customer authorization belongs to provisioning and survives a factory reset.
CI4RAIL_OTA_PROTECTION ?= "0"

do_install:append() {
    if [ "${CI4RAIL_OTA_PROTECTION}" = "1" ]; then
        echo '/data/mender/mender.conf' >> ${D}${sysconfdir}/factory-reset-excludes.conf
        echo '/data/ci4rail/ota' >> ${D}${sysconfdir}/factory-reset-excludes.conf
    fi
}
