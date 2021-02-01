# This should probably go into a BSP layer
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://eth.cfg"