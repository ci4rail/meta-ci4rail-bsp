SUMMARY = "Create persistent directories for k8s and openyurt"
DESCRIPTION = "Create persistent directories for k8s and openyurt"
HOMEPAGE = "https://ci4rail.com"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI = "file://${BPN}.service"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit systemd
inherit features_check

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${PN}.service ${D}${systemd_unitdir}/system/
}

REQUIRED_DISTRO_FEATURES= "systemd"

