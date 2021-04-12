FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://dropbear.default \
file://authorized_keys \
"

FILES_${PN} += "/home/root/.ssh/authorized_keys"

do_install_append() {
    install -m 0644 ${WORKDIR}/dropbear.default ${D}/etc/default/dropbear
    install -d ${D}/home/root/.ssh
    install -m 0644 ${WORKDIR}/authorized_keys ${D}/home/root/.ssh/authorized_keys
}

