# From https://github.com/openembedded/meta-openembedded/blob/master/meta-oe/recipes-connectivity/libqmi/libqmi_1.34.0.bb

SUMMARY = "libqmi is a library for talking to WWAN devices by QMI protocol"
DESCRIPTION = "libqmi is a glib-based library for talking to WWAN modems and \
               devices which speak the Qualcomm MSM Interface (QMI) protocol"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libqmi"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
"

DEPENDS = "glib-2.0 glib-2.0-native"

GNOMEBASEBUILDCLASS = "meson"
inherit gnomebase gettext systemd gobject-introspection bash-completion

SRCREV = "3f07d6e5b4677558543b3b4484ea88ad92257e92"
SRC_URI = "git://gitlab.freedesktop.org/mobile-broadband/libqmi.git;protocol=https;branch=qmi-1-34"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= "udev mbim"
PACKAGECONFIG[udev] = "-Dudev=true,-Dudev=false,libgudev"
PACKAGECONFIG[mbim] = "-Dmbim_qmux=true,-Dmbim_qmux=false,libmbim"
PACKAGECONFIG[qrtr] = "-Dqrtr=true,-Dqrtr=false,libqrtr-glib"

EXTRA_OEMESON = " \
    -Dgtk_doc=false \
    -Dman=false \
"