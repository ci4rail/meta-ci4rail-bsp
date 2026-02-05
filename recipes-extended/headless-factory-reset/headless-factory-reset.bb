SUMMARY = "Headless factory reset target and service"
DESCRIPTION = "Provides a systemd target and service to reset the system to factory condition in headless mode"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://factory-reset \
           file://headless-reset-system.target \
           file://reset-data-partition \
           file://reset-data-partition.service \
           file://reset-data-partition-failed.service \
           file://factory-reset-excludes.conf \
          "

inherit systemd
inherit features_check

SYSTEMD_SERVICE:${PN} = " \
    reset-data-partition-failed.service \
    reset-data-partition.service \
    headless-reset-system.target \
    "
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    # systemd units
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/headless-reset-system.target ${D}${systemd_system_unitdir}/
    install -m 0644 ${WORKDIR}/reset-data-partition.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${WORKDIR}/reset-data-partition-failed.service ${D}${systemd_system_unitdir}/

    # configuration
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/factory-reset-excludes.conf ${D}${sysconfdir}/

    # reset scripts
    install -d ${D}${libexecdir}
    install -d ${D}${sbindir}
    install -m 0700 ${WORKDIR}/reset-data-partition ${D}${libexecdir}
    install -m 0755 ${WORKDIR}/factory-reset ${D}${sbindir}
}



FILES:${PN} += "\
    ${systemd_system_unitdir}/headless-reset-system.target \
    ${systemd_system_unitdir}/reset-data-partition.service \
    ${systemd_system_unitdir}/reset-data-partition-failed.service \
    ${sysconfdir}/factory-reset-excludes.conf \
    ${libexecdir}/reset-data-partition \
    ${sbindir}/factory-reset \
"

RDEPENDS:${PN} += "bash util-linux-findmnt util-linux-umount"

REQUIRED_DISTRO_FEATURES = "systemd"