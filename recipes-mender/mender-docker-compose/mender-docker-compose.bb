FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "file://app \
            file://docker-compose \
            file://mender-app.conf"

RDEPENDS:${PN} += "bash jq xdelta3 tree"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

do_install:append() {
    install -d ${D}${sysconfdir}/mender
    install -m 0644 ${WORKDIR}/mender-app.conf ${D}${sysconfdir}/mender/mender-app.conf
    install -d ${D}/usr/share/mender/modules/v3/
    install -d ${D}/usr/share/mender/app-modules/v1/
    install -m 0755 ${WORKDIR}/app ${D}/usr/share/mender/modules/v3/
    install -m 0755 ${WORKDIR}/docker-compose ${D}/usr/share/mender/app-modules/v1/
}

FILES:${PN} += "/usr/share/mender/modules/v3/app"
FILES:${PN} += "/usr/share/mender/app-modules/v1/docker-compose"

CONFFILES:${PN} += "${sysconfdir}/mender/mender-app.conf"
