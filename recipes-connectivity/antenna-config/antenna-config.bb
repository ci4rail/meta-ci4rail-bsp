SUMMARY = "Deployment of Antenna configurations"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://set-antenna-config.service \
    file://set_antenna_config \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "set-antenna-config.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/set-antenna-config.service ${D}${systemd_unitdir}/system/
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/set_antenna_config ${D}${sysconfdir}/
}

FILES:${PN} += " \
    ${systemd_unitdir}/system/set-antenna-config.service \
    ${sysconfdir}/set_antenna_config \
"
