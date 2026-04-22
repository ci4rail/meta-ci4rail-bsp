FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Align linux-toradex with the kernel CVE workflow described in the Yocto
# security manual by importing the generic and version-specific kernel CVE
# status metadata.
include linux-toradex-cve-exclusion_6.6.inc

# Enable kernel debug metadata so SPDX/debugsources can be used by the kernel
# CVE post-processing workflow to filter config-dependent false positives.
KERNEL_EXTRA_FEATURES:append = " features/debug/debug-kernel.scc"

SRC_URI:append:verdin-imx8mp = " file://moducop-cpu01plus-standard.scc" 
SRC_URI:append:verdin-imx8mm = " file://moducop-cpu01-standard.scc" 

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

