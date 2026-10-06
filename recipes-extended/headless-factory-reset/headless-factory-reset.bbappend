# Preserve persistent Mender configuration. Customer keys are restored from the
# os-customization factory layer mounted at /etc.
CI4RAIL_OTA_PROTECTION ?= "0"

do_install:append() {
    if [ "${CI4RAIL_OTA_PROTECTION}" = "1" ]; then
        echo '/data/mender/mender.conf' >> ${D}${sysconfdir}/factory-reset-excludes.conf
    fi
}
