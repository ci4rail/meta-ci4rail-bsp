inherit systemd
inherit features_check

DESCRIPTION = "Moducop CPU01 Layout0 remove GPS reset service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI += "file://gps-cpu01-lay0-reset.service"


SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "gps-cpu01-lay0-reset.service"
REQUIRED_DISTRO_FEATURES= "systemd"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/gps-cpu01-lay0-reset.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += "${systemd_system_unitdir}/gps-cpu01-lay0-reset.service"