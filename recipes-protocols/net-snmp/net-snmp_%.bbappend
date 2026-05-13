FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://snmp-symlink.service"

do_install:append () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/snmp-symlink.service ${D}${systemd_unitdir}/system/
}

FILES:${PN}-server-snmpd:append = " ${systemd_unitdir}/system/snmp-symlink.service"
SYSTEMD_SERVICE:${PN}-server-snmpd:append = " snmp-symlink.service"
SYSTEMD_AUTO_ENABLE:${PN}-server-snmpd = "enable"