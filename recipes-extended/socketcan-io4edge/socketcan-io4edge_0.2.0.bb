SUMMARY = "Socketcan support for Io4edge CANL2 devices"
DESCRIPTION = "Gateway between virtual socket CAN and io4edge CAN"
HOMEPAGE = "https://github.com/ci4rail/socketcan-io4edge"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI_x86-64 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI_arm = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI_i586 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_i686 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_aarch64 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[386.md5sum] = "2a85edcf0dd0e68db700bab6506ff78c"
SRC_URI[386.sha256sum] = "83479497661f90edeabbccf5a41482e3f5bfef1c854b04d1063762c152a05a71"
SRC_URI[amd64.md5sum] = "9fa3f36be59103c5e1209400843feaea"
SRC_URI[amd64.sha256sum] = "cdb629871fc592b69178c6f66ba83b6f87fc302d4cff0dde8b1d05b427f405e2"
SRC_URI[arm64.md5sum] = "be5053b4297cb3608e7cc0ba768e8ece"
SRC_URI[arm64.sha256sum] = "c171f7851c1e7561be5e637cac0c4ae91edc8ebf86e5a2944c615d11c494cb37"
SRC_URI[arm.md5sum] = "b4d6c86aac4121666301db652487f33e"
SRC_URI[arm.sha256sum] = "176959f7f199ecdf3977bdcaffebc7fadccf5ebd8942f6e47705ec87e0d1b9f4"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/${BPN} ${D}${bindir}/${BPN}
}

FILES_${PN} += "${bindir}/${BPN}"
