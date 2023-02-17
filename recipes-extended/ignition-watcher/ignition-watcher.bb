inherit systemd
inherit features_check

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://ignition-watcher.service \
            file://watch_ignition_pin.sh"

SYSTEMD_SERVICE_${PN} = "ignition-watcher.service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${bindir}
    install -m 0644 ${WORKDIR}/ignition-watcher.service ${D}${systemd_system_unitdir}
    install -m 0755 ${WORKDIR}/watch_ignition_pin.sh ${D}${bindir}
}

FILES_${PN} += "${bindir}/watch_ignition_pin.sh"
FILES_${PN} += "${systemd_system_unitdir}/ignition-watcher.service"

REQUIRED_DISTRO_FEATURES= "systemd"
