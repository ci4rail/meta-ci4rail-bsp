require linux-toradex-kmeta.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://eth.cfg \
            file://edgefarm.cfg \
            file://modem.cfg \
            file://ignition.cfg \
            file://0001-verdin-pcie-clockstrength.patch \
            file://0003-reconfigure-moducop-CPU01-ETH0-LED.patch \
            file://0004-lan7431-fixed-phy-support.patch \
            file://0005-lan743x-Backport-from-linux-vanilla-kernel-e20a471.patch \
            file://0006-add-moducop-ignition-shutdown-handler.patch \
            file://0007-Provide-standard-sensor-monitoring-via-HWMON.patch \
            "
