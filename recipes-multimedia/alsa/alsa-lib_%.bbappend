FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://alsa.conf"

do_install:append () {
    install -d ${D}${datadir}
    install -m 0644 ${WORKDIR}/alsa.conf ${D}${datadir}/alsa/alsa.conf
}
