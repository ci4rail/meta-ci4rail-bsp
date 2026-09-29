SUMMARY = "Persistent logging on the Mender data partition"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://persistent-logging-prepare.service file://var-log.mount file://60-persistent.conf"
inherit allarch systemd
SYSTEMD_SERVICE:${PN} = "var-log.mount"
RDEPENDS:${PN} = "systemd"
do_install() {
    install -d ${D}${systemd_system_unitdir} ${D}${sysconfdir}/systemd/journald.conf.d
    install -m 0644 ${WORKDIR}/persistent-logging-prepare.service ${WORKDIR}/var-log.mount ${D}${systemd_system_unitdir}/
    install -m 0644 ${WORKDIR}/60-persistent.conf ${D}${sysconfdir}/systemd/journald.conf.d/
}
FILES:${PN} += "${systemd_system_unitdir}"
