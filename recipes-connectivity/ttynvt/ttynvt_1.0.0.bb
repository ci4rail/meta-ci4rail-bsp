LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://gitlab.com/lars-thrane-as/ttynvt.git"
SRC_URI += "file://${PN}.service"

SRCREV = "f759c0e3cc28fa9730f3692a897b7f3e311ada38"
PV = "0.0.1+git${SRCPV}"

DEPENDS += "fuse"
RDEPENDS_${PN} += "fuse"

S = "${WORKDIR}/git"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "${PN}.service"
FILES_${PN} += "${systemd_system_unitdir}/${PN}.service"

inherit autotools pkgconfig systemd features_check