FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://dropbear.default"

do_install_append() {
    install -m 0644 ${WORKDIR}/dropbear.default ${D}/etc/default/dropbear
}
