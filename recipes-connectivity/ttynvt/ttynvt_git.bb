LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"


SUMMARY = "tty over network"
DESCRIPTION = "ttynvt makes a virtual serial device (tty) and connects the device to a Network Virtual Terminal (NVT)"
HOMEPAGE = "https://gitlab.com/ci4rail/ttynvt"

SRC_URI = "git://gitlab.com/ci4rail/ttynvt.git;branch=fix/odd-parity;protocol=https"

SRCREV = "86556848b6f74debe48da077e18f0e6437478528"
PV = "0.0.1+git${SRCPV}"

DEPENDS += "fuse"
RDEPENDS:${PN} += "fuse"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
