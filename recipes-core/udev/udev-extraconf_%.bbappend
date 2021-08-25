FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://99-esp32-netif-naming.rules \
            file://esp_interface_name.sh"

do_install_append() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/udev/rules.d

    install -m 0644 ${WORKDIR}/99-esp32-netif-naming.rules ${D}${sysconfdir}/udev/rules.d/99-esp32-netif-naming.rules
    install -m 0644 ${WORKDIR}/esp_interface_name.sh ${D}${bindir}/esp_interface_name.sh

    chmod +x ${D}${bindir}/esp_interface_name.sh
}

FILES_${PN} += "${sysconfdir}/udev/rules.d/99-esp32-netif-naming.rules"
FILES_${PN} += "${bindir}/esp_interface_name.sh"
