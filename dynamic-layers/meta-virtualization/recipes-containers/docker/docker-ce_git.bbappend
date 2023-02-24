# Add a `/etc/docker/daemon.json` with a modified root-dir and a specific dns server.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = " file://daemon.json"

do_install:append() {
    install -m 0644 ${WORKDIR}/daemon.json ${D}${sysconfdir}/docker/daemon.json
}

FILES:${PN}:append = " ${sysconfdir}/docker/daemon.json"
