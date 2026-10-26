SUMMARY = "Apply user-provided /etc/nftables.conf at startup"
DESCRIPTION = "If a /etc/nftables.conf is deployed (e.g. via os-customization), it is applied during boot"

inherit systemd features_check

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://nftables-apply.service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

S = "${WORKDIR}"

SYSTEMD_SERVICE:${PN} = "nftables-apply.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/nftables-apply.service ${D}${systemd_system_unitdir}
}

FILES:${PN} += "${systemd_system_unitdir}/nftables-apply.service"

RDEPENDS:${PN} += "nftables kernel-module-nf-tables"

REQUIRED_DISTRO_FEATURES = "systemd"
