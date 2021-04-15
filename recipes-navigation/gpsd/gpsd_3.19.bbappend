FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://etc_default_gpsd"

# For some reason, the original gpsd recipe doesn't start gpsd.service automatically (but gpsd.socket and gpsdctl@service)
SYSTEMD_SERVICE_${PN} = "${BPN}.service ${BPN}.socket ${BPN}ctl@.service"

do_install_append() {
     install -d ${D}/${sysconfdir}/default
     install -m 0644 ${WORKDIR}/etc_default_gpsd ${D}/${sysconfdir}/default/gpsd.default
}
