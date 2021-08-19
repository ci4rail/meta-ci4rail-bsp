DESCRIPTION = "Device Firmware Update via Network for Ci4Rail NetIO Modules"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://netio_dfu.py file://ca_cert.pem file://ca_key.pem"

RDEPENDS_${PN} = "python3 python3-core"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/netio_dfu.py ${D}${bindir}/
    install -d ${D}${libdir}/${PN}
    install -m 0755 ${WORKDIR}/ca*.pem ${D}${libdir}/${PN}
}

FILES_${PN} += "${bindir}/netio_dfu.py ${libdir}/${PN}/ca_cert.pem ${libdir}/${PN}/ca_key.pem"
