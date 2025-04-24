FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://99-usb-netif-setup.rules \
            file://usb-netif-interface-name.sh \
            file://usb-netif-setup.sh \
            file://99-tty-static-names.rules"

do_install:append() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/udev/rules.d

    install -m 0644 ${WORKDIR}/99-usb-netif-setup.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0755 ${WORKDIR}/usb-netif-interface-name.sh ${D}${bindir}/
    install -m 0755 ${WORKDIR}/usb-netif-setup.sh ${D}${bindir}/

    install -m 0644 ${WORKDIR}/99-tty-static-names.rules ${D}${sysconfdir}/udev/rules.d
}

FILES:${PN} += "${sysconfdir}/udev/rules.d/99-usb-netif-setup.rules"
FILES:${PN} += "${bindir}/usb-netif-setup.sh"
FILES:${PN} += "${bindir}/usb-netif-interface-name.sh"
FILES:${PN} += "${sysconfdir}/udev/rules.d/99-tty-static-names.rules"
RDEPENDS:${PN} += "bash"
