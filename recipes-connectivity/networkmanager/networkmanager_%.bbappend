FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

#SRC_URI += "file://toradex-nmconnection.conf file://network.nmconnection.in"

PACKAGECONFIG_remove = "ifupdown dnsmasq"

PACKAGECONFIG_append = " modemmanager ppp"
RPROVIDES_${PN} = "network-configuration"

