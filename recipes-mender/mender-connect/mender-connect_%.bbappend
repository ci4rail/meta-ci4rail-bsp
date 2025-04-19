FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://mender-connect.conf"

RDEPENDS_${PN} += "bash"

do_install:append() {
    install -m 0600 ${WORKDIR}/mender-connect.conf ${D}${sysconfdir}/mender/
}