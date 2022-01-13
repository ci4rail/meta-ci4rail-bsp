# Add a `/etc/docker/daemon.json` with a modified root-dir and a specific dns server.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = " file://daemon.json"

do_install_append() {
    install -m 0644 ${WORKDIR}/daemon.json ${D}${sysconfdir}/docker/daemon.json
}

FILES_${PN}_append = " ${sysconfdir}/docker/daemon.json"
