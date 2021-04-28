# This should probably go into a BSP layer
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://eth.cfg \
            file://modem.cfg \
            file://0001-verdin-pcie-clockstrength.patch"
#SRC_URI_moducop-cpu01 += "file://0001-verdin-pcie-clockstrength.patch"
