FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://asound.state \
            file://asound.conf \
            file://state-daemon.conf"

FILES:${PN} += "${sysconfdir}/alsa/state-daemon.conf  \
                ${sysconfdir}/asound.conf"

FILES:alsa-states = "${sysconfdir}/alsa/asound.state"

do_install:append () {
    rm -rf ${D}${localstatedir}
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/alsa
    install -m 0644 ${WORKDIR}/asound.state ${D}${sysconfdir}/alsa
    install -m 0644 ${WORKDIR}/state-daemon.conf ${D}${sysconfdir}/alsa
    install -m 0644 ${WORKDIR}/asound.conf ${D}${sysconfdir}
}
