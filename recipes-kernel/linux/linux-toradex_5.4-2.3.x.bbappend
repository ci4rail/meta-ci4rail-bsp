require linux-toradex-kmeta.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://eth.cfg \
            file://modem.cfg \
            file://0001-verdin-pcie-clockstrength.patch \
            file://0002-verdin-pcie-reset.patch \
            file://0003-reconfigure-moducop-CPU01-ETH0-LED.patch \
            file://0004-lan7431-fixed-phy-support.patch \
            "
