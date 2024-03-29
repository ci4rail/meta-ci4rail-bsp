FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SYSTEMD_AUTO_ENABLE_${PN}-server = "enable"

# override this in your local.conf to add additional interfaces to the dhcp server
DHCPD_ADDITIONAL_INTERFACES ?= ""

do_install_append() {
    if [ -n "${DHCPD_ADDITIONAL_INTERFACES}" ]; then
        sed -i "s/INTERFACES=\"/INTERFACES=\"${DHCPD_ADDITIONAL_INTERFACES} /g" ${D}${sysconfdir}/default/dhcp-server
    fi
}