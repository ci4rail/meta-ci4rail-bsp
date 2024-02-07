FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

# Set fixed IP for eth2
SRC_URI += "file://eth2.connection \
            file://ethernet-usb_io_ctrl.nmconnection \
            file://ethernet-usb_ext1.nmconnection \
            file://ethernet-usb_ext2.nmconnection \
            file://ethernet-usb_ext3.nmconnection \
            file://ethernet-usb_ext4.nmconnection \
            file://ethernet-usb_ext5.nmconnection \
            file://ethernet-usb_ext6.nmconnection \
            file://ethernet-usb_ext7.nmconnection \
            file://ethernet-usb_ext8.nmconnection \
            file://10-dhcpd-restart.sh \
            "

PACKAGECONFIG_remove = "ifupdown dnsmasq"

# configure networkmanager with modemmanager support
PACKAGECONFIG_append = " modemmanager ppp"
RPROVIDES_${PN} = "network-configuration"
RDEPENDS_${PN} += "bash"

do_install_append() {
    install -m 0600 ${WORKDIR}/eth2.connection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_io_ctrl.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext1.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext2.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext3.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext4.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext5.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext6.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext7.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0600 ${WORKDIR}/ethernet-usb_ext8.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -m 0755 ${WORKDIR}/10-dhcpd-restart.sh ${D}${sysconfdir}/NetworkManager/dispatcher.d/
}
