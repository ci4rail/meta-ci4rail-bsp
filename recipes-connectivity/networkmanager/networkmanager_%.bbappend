FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Set fixed IP for eth2
# Don't manage io4edge usb-ethernet interfaces
SRC_URI += "file://eth2.connection \
            file://usb-netif-unmanaged.conf \
            "


# configure networkmanager with modemmanager support
PACKAGECONFIG:append = " modemmanager ppp wwan wifi"
RPROVIDES:${PN} = "network-configuration"
RDEPENDS:${PN} += "bash"

do_install:append() {
    install -m 0600 ${WORKDIR}/eth2.connection ${D}${sysconfdir}/NetworkManager/system-connections/
    install -d ${D}${sysconfdir}/NetworkManager/conf.d/
    install -m 0600 ${WORKDIR}/usb-netif-unmanaged.conf  ${D}${sysconfdir}/NetworkManager/conf.d/
}

