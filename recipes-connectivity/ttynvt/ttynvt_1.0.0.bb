LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"


SUMMARY = "tty over network"
DESCRIPTION = "ttynvt makes a virtual serial device (tty) and connects the device to a Network Virtual Terminal (NVT)"
HOMEPAGE = "https://gitlab.com/ci4rail/ttynvt"

SRC_URI = "git://gitlab.com/ci4rail/ttynvt.git;branch=add_rs485_support;protocol=https"

SRCREV = "de626fcbf3876dc29755857f956549ab8d27f36c"
PV = "0.0.1+git${SRCPV}"

DEPENDS += "fuse"
RDEPENDS_${PN} += "fuse"

S = "${WORKDIR}/git"

inherit autotools pkgconfig features_check
