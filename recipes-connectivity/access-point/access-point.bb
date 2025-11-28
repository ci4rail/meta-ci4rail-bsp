SUMMARY = "Deployment of Access Point configurations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RDEPENDS:${PN} = "hostapd"

S = "${WORKDIR}"

SRC_URI = " \
    file://access-point.service \
    file://access-point.network \
    file://access-point.conf \
"

inherit allarch systemd

SYSTEMD_SERVICE:${PN} = "access-point.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

do_install() {
    install -d ${D}${systemd_unitdir}/system/ ${D}${sysconfdir}/ ${D}${sysconfdir}/systemd/network/ ${D}${bindir}
    install -m 0644 access-point.network ${D}${sysconfdir}/systemd/network/
    install -m 0644 access-point.service ${D}${systemd_unitdir}/system/
    install -m 0644 access-point.conf ${D}${sysconfdir}/
    install -m 0644 shared-access-point ${D}${bindir}/
    sed -i -e 's,@SBINDIR@,${sbindir},g' -e 's,@SYSCONFDIR@,${sysconfdir},g' ${D}${systemd_unitdir}/system/access-point.service
}

FILES:${PN} += " \
    ${systemd_unitdir}/system/* \
    ${sysconfdir}/systemd/network/access-point.network \
    ${sysconfdir}/access-point.conf \
"

