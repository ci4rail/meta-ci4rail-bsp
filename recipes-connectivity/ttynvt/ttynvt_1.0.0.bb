LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://gitlab.com/ci4rail/ttynvt.git;branch=split-read_net;protocol=https"
SRC_URI += "file://${BPN}.service"

SRCREV = "0e8a4c83a0409568fbec60c3479534473dc4d3ab"
PV = "0.0.1+git${SRCPV}"

DEPENDS += "fuse"
RDEPENDS_${PN} += "fuse"

S = "${WORKDIR}/git"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${BPN} = "${BPN}.service"
FILES_${PN} += "${systemd_system_unitdir}/${PN}.service"

do_install_append() {
  install -d ${D}/${systemd_unitdir}/system
  install -m 0644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system
}

inherit autotools pkgconfig systemd features_check