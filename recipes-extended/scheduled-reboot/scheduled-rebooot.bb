inherit systemd
inherit features_check

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://scheduled-reboot.service \
            file://watch_reboot_time.sh"

SYSTEMD_SERVICE_${PN} = "scheduled-reboot.service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${bindir}
    install -m 0644 ${WORKDIR}/scheduled-reboot.service ${D}${systemd_system_unitdir}
    install -m 0755 ${WORKDIR}/watch_reboot_time.sh ${D}${bindir}
}

FILES_${PN} += "${bindir}/watch_reboot_time.sh"
FILES_${PN} += "${systemd_system_unitdir}/scheduled-reboot.service"

REQUIRED_DISTRO_FEATURES= "systemd"
