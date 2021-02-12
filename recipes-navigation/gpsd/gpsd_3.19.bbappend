FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
#COMPATIBLE_MACHINE = "moducop-cpu01 verdin-imx8mm"
SRC_URI += "file://etc_default_gpsd"

do_install_append() {
     install -d ${D}/${sysconfdir}/default
     install -m 0644 ${WORKDIR}/etc_default_gpsd ${D}/${sysconfdir}/default/gpsd.default
}