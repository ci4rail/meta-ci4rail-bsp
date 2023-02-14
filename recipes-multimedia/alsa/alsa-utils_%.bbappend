FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://alsa-state.service"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${BPN} = "alsa-state.service"

do_install_append () {
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/alsa-state.service ${D}/${systemd_unitdir}/system
}
