FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://etc_default_gpsd \
            file://gpsd.service \
            file://gpsd.socket"

#SYSTEMD_SERVICE_${PN} = "${BPN}.service ${BPN}.socket ${BPN}ctl@.service"
#SYSTEMD_SERVICE_${PN} = "${BPN}.service ${BPN}ctl@.service"

do_install_append() {
     install -d ${D}/${sysconfdir}/default
     install -m 0644 ${WORKDIR}/etc_default_gpsd ${D}/${sysconfdir}/default/gpsd.default
     install -m 0644 ${WORKDIR}/${BPN}.socket ${D}${systemd_unitdir}/system/${BPN}.socket
     install -m 0644 ${WORKDIR}/${BPN}.service ${D}${systemd_unitdir}/system/${BPN}.service
     # Prevent ModemManager to use the GPS device
     sed -i '/^ATTRS/ s/ENV{SYSTEMD_WANTS}="gpsdctl@%k.service"/ENV{SYSTEMD_WANTS}="gpsdctl@%k.service", ENV{ID_MM_DEVICE_IGNORE}="1"/g' ${D}/${sysconfdir}/udev/rules.d/gpsd.rules
}
