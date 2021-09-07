require linux-toradex-kmeta.inc
# This should probably go into a BSP layer
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://eth.cfg \
            file://modem.cfg \
            file://0001-verdin-pcie-clockstrength.patch \
            file://0002-qcserial-em7421.patch \
            "
