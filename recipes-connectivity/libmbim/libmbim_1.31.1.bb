# From https://github.com/openembedded/meta-openembedded/blob/master/meta-oe/recipes-connectivity/libmbim/libmbim_1.31.1.bb
SUMMARY = "libmbim is library for talking to WWAN devices by MBIM protocol"
DESCRIPTION = "libmbim is a glib-based library for talking to WWAN modems and devices which speak the Mobile Interface Broadband Model (MBIM) protocol"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libmbim/"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = " \
    file://LICENSES/GPL-2.0-or-later.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://LICENSES/LGPL-2.1-or-later.txt;md5=4fbd65380cdd255951079008b364516c \
"

DEPENDS = "glib-2.0 glib-2.0-native libgudev"

inherit meson pkgconfig bash-completion gobject-introspection

SRCREV = "db0f5a09d4d29145620b81df0fdd8f4786a8fb59"
SRC_URI = "git://gitlab.freedesktop.org/mobile-broadband/libmbim.git;protocol=https;branch=main"

S = "${WORKDIR}/git"

EXTRA_OEMESON = " \
    -Dgtk_doc=false \
    -Dman=false \
"