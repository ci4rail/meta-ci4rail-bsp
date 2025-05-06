FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://etc_default_gpsd \
            file://gpsd.service \
            file://gpsd.socket \
            file://gpsd.rules"

do_install:append() {
     install -d ${D}/${sysconfdir}/default
     install -m 0644 ${WORKDIR}/etc_default_gpsd ${D}/${sysconfdir}/default/gpsd.default
     install -m 0644 ${WORKDIR}/${BPN}.socket ${D}${systemd_unitdir}/system/${BPN}.socket
     install -m 0644 ${WORKDIR}/${BPN}.service ${D}${systemd_unitdir}/system/${BPN}.service
     install -d ${D}/${sysconfdir}/udev/rules.d
     install -m 0644 ${WORKDIR}/gpsd.rules ${D}/${sysconfdir}/udev/rules.d/
}
