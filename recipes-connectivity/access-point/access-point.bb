SUMMARY = "Deployment of Access Point configurations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RDEPENDS:${PN} = "hostapd networkmanager"

S = "${WORKDIR}"

SRC_URI = " \
    file://shared-access-point \
    file://setup-dhcp-uap0 \
    file://access-point.service \
    file://access-point.network.active \
    file://access-point.conf \
"

inherit allarch systemd

SYSTEMD_SERVICE:${PN} = "access-point.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

do_install() {
    install -d ${D}${systemd_unitdir}/system/ ${D}${sysconfdir}/ ${D}${sysconfdir}/systemd/network/ ${D}${bindir}
    install -m 0644 access-point.network.active ${D}${sysconfdir}/
    install -m 0644 access-point.service ${D}${systemd_unitdir}/system/
    install -m 0644 access-point.conf ${D}${sysconfdir}/
    install -m 0755 shared-access-point ${D}${bindir}/
    install -m 0755 setup-dhcp-uap0 ${D}${bindir}/
    sed -i -e 's,@SBINDIR@,${sbindir},g' -e 's,@SYSCONFDIR@,${sysconfdir},g' ${D}${systemd_unitdir}/system/access-point.service
}

FILES:${PN} += " \
    ${systemd_unitdir}/system/* \
    ${sysconfdir}/access-point.network.active \
    ${sysconfdir}/access-point.conf \
    ${bindir}/shared-access-point \
    ${bindir}/setup-dhcp-uap0 \
"

