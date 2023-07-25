SUMMARY = "Restart ModemManager if no modem found"
DESCRIPTION = "Check some time after boot if a modem is found. If not, restart ModemManager."
HOMEPAGE = "https://ci4rail.com"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI_append = " file://${BPN}.service \
                   file://modem-restart-check.sh \
                   "

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "\
    ${BPN}.service \
"

FILES_${PN} += "${systemd_system_unitdir}/${BPN}.service"
FILES_${PN} += "${bindir}/modem-restart-check.sh"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/modem-restart-check.sh ${D}${bindir}
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system
}

inherit systemd