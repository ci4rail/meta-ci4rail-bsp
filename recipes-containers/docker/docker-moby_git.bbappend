# Add a `/etc/docker/daemon.json` with a modified root-dir and a specific dns server.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += " file://daemon.json"

SYSTEMD_SERVICE:${PN} = "docker.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install:append() {
    install -d ${D}${sysconfdir}/docker
    install -m 0644 ${WORKDIR}/daemon.json ${D}${sysconfdir}/docker/daemon.json
}

FILES:${PN}:append = " ${sysconfdir}/docker/daemon.json"
