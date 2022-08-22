SUMMARY = "Command Line Interface for Io4Edge Devices"
DESCRIPTION = "Command line tool to manage io4edge devices, e.g. identify the currently running firmware, load new firmware, etc."
HOMEPAGE = "https://github.com/ci4rail/io4edge-cli"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI_x86-64 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI_arm = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI_i586 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_i686 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_aarch64 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"

SRC_URI[386.md5sum] = "adc8f11f2b545f34c0082e423f88af21"
SRC_URI[386.sha256sum] = "694694db4f51e6ed6522d37b492c28e4b41afca778c8f184b2414377f040d297"
SRC_URI[amd64.md5sum] = "365fa0dc4977a5828a17247820671800"
SRC_URI[amd64.sha256sum] = "684c1131ce88f01a5389b1e0f6178f736f7db737917c495e268f5e1c4a0fba1f"
SRC_URI[arm64.md5sum] = "8aba92fbebbbf263a18a737fd13b80e8"
SRC_URI[arm64.sha256sum] = "84932c1b449003156474165ea7c3b2bdb8bd57b14300ea759171096204ba0b7a"
SRC_URI[arm.md5sum] = "7f2ac83f85390b39204ff4340de2ed77"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/io4edge-cli ${D}${bindir}/io4edge-cli
}

FILES_${PN} += "${bindir}/io4edge-cli"
