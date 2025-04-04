FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://dropbear.default"

do_install:append() {
    install -m 0644 ${WORKDIR}/dropbear.default ${D}/etc/default/dropbear
}
