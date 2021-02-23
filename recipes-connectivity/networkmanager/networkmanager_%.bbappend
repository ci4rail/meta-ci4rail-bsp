FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

# Set fixed IP for eth2
SRC_URI += "file://eth2.connection"

PACKAGECONFIG_remove = "ifupdown dnsmasq"

# configure networkmanager with modemmanager support
PACKAGECONFIG_append = " modemmanager ppp"
RPROVIDES_${PN} = "network-configuration"

do_install_append() {
    install -m 0600 ${WORKDIR}/eth2.connection ${D}${sysconfdir}/NetworkManager/system-connections/
}
