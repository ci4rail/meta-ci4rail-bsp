# install dnsmasq config
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += " file://dnsmasq.conf"

do_install:append() {
  	# Force the main dnsmasq instance to bind only to specified interfaces and
	# to not bind to podman*. podman will run its own instance on this interface.
	install -d ${D}/${sysconfdir}/dnsmasq.d
	install -m 644 ${WORKDIR}/dnsmasq.conf ${D}/${sysconfdir}/dnsmasq.d/podman
}