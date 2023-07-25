FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://ModemManager.service"

do_install_append() {
     install -m 0644 ${WORKDIR}/ModemManager.service ${D}${systemd_unitdir}/system/ModemManager.service
}
