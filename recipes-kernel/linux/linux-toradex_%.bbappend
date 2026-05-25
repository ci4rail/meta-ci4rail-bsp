FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
# Keep the ci4rail kernel patches active across Toradex BSP point releases.
SRC_URI:append:verdin-imx8mp = " file://moducop-cpu01plus/moducop-cpu01plus-standard.scc"
SRC_URI:append:verdin-imx8mm = " file://moducop-cpu01/moducop-cpu01-standard.scc"

SRC_URI:append = " file://0001-moducop-specific-dts.patch \
                   file://0002-add-moducop-dts-to-Makefile.patch \
                   file://0003-reconfigure-moducop-CPU01-ETH0-LED.patch \
                   file://0004-add-moducop-ignition-shutdown-handler-with-char-dev.patch \
                   file://modem.cfg \
                   file://eth2.cfg \
                   file://netext.cfg \
                   file://general.cfg \
                   file://ignition.cfg \
                   file://sound.cfg \
                "
