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

SRC_URI[386.md5sum] = "c4d6752db0409f9d858a1ab7967c8b9d"
SRC_URI[386.sha256sum] = "041e7dfe197b08ef9a06ff0af19b90a1e68ccf1994a6381cfdfa32face1c5ed7"
SRC_URI[amd64.md5sum] = "d02b277d87e657484d5ee3d89dd1b0d8"
SRC_URI[amd64.sha256sum] = "82dee3f934cae6f945c99d7b746a5466cafa3d9d38797d79787f4dcd37a1a513"
SRC_URI[arm64.md5sum] = "9eeb34901d7b60f9168671bf7d94fc33"
SRC_URI[arm64.sha256sum] = "b803fdd257ff12a4e145e49a51dabe2d047f2a5b8411df1e9852420f170812df"
SRC_URI[arm.md5sum] = "2c61e4408b7b0c3336e510e9a41dc6fa"
SRC_URI[arm.sha256sum] = "b131b8faecc4aab724e8291ccbcab5e9315bb8e94e0e2640069faeeb785f10df"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/io4edge-cli ${D}${bindir}/io4edge-cli
}

FILES_${PN} += "${bindir}/io4edge-cli"
