FILESEXTRAPATHS:prepend:mender-uboot := "${THISDIR}/files:${THISDIR}/files/${TORADEX_BSP_VERSION}:"

MENDER_TORADEX_USE_BOOTSCR ??= "0"

SRC_URI:append:mender-uboot = "${@bb.utils.contains('MENDER_TORADEX_USE_BOOTSCR', '1', ' file://0002-No-kernel-messages-on-serial.patch;patchdir=${WORKDIR};striplevel=0', '', d)}"
