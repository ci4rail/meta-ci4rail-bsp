PACKAGE_WRITE_DEPS_append = " ${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd-systemctl-native','',d)}"
pkg_postinst_${PN}_append () {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		if [ -n "$D" ]; then
			OPTS="--root=$D"
		fi

		# Mask systemd-networkd-wait-online.service to avoid long boot times
		# when networking is unplugged
		systemctl $OPTS mask systemd-networkd-wait-online.service
	fi
}


do_install_append() {
	# Patch /lib/systemd/system/systemd-rfkill.service with additional dependency
	# /var must be writeable when rfkill service is started
	sed -i -e 's/After=sys-devices-virtual-misc-rfkill.device systemd-remount-fs.service/After=After=sys-devices-virtual-misc-rfkill.device systemd-remount-fs.service reload-systemd.service/' ${D}${systemd_unitdir}/system/systemd-rfkill.service
}