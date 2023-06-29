#
# Disable systemd service. 
# dnsmasq is started by NetworkManager, not via systemd
#
SYSTEMD_SERVICE_${PN} = ""

do_install_append() {
    rm -rf ${D}/lib
}