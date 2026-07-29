FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://78-mm-simcom-8230-port-types.rules"

do_install:append() {
    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/78-mm-simcom-8230-port-types.rules \
        ${D}${nonarch_base_libdir}/udev/rules.d/
}
