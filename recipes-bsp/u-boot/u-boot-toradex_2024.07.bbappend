FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Keep this board policy out of other Toradex machines using this layer.
SRC_URI:append:moducop-cpu01 = " \
    file://0001-moducop-boot-sd-recovery-before-environment.patch \
    file://0002-mmc-retry-recovery-sd-cmd8.patch \
"
SRC_URI:append:moducop-cpu01plus = " \
    file://0001-moducop-boot-sd-recovery-before-environment.patch \
    file://0002-mmc-retry-recovery-sd-cmd8.patch \
"
