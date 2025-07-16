FILESEXTRAPATHS:prepend:mender-uboot := "${THISDIR}/files:${THISDIR}/files/${TORADEX_BSP_VERSION}:"

SRC_URI += "file://0002-No-kernel-messages-on-serial.patch;patchdir=${WORKDIR};striplevel=0"