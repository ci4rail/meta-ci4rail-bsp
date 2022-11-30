# Invalidate the defaults from meta-toradex-nxp, as we don't use the IMX8 internal sound device
# Futhermore, the meta-toradex-nxp pkg_postinst_ontarget_* will result in a failure when building the rootfs
# "The following packages could not be configured offline and rootfs is read-only: ['alsa-state']"

do_install_append_mx8m_tdx () {
}

pkg_postinst_${PN}_mx8m_tdx () {
}

pkg_postinst_ontarget_${PN}_mx8m_tdx () {
}