FILESEXTRAPATHS_prepend_mender-uboot := "${THISDIR}/files:${THISDIR}/files/${TORADEX_BSP_VERSION}:"

SRC_URI += "file://0001-Fix-uboot-issue.patch;patchdir=${WORKDIR};striplevel=0"