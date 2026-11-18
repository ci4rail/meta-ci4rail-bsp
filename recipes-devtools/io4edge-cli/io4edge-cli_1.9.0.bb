SUMMARY = "Command Line Interface for Io4Edge Devices"
DESCRIPTION = "Command line tool to manage io4edge devices, e.g. identify the currently running firmware, load new firmware, etc."
HOMEPAGE = "https://github.com/ci4rail/io4edge-client-go"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

inherit go-prebuilt-clearing

SRC_URI:append = " https://github.com/ci4rail/io4edge-client-go/archive/refs/tags/v${PV}.tar.gz;name=clearing_source;downloadfilename=${BPN}-${PV}-source.tar.gz;subdir=clearing-source"
SRC_URI[clearing_source.sha256sum] = "d63f6df9a8d8953e60f2ce5d32012b9327d52c3eea30e3bc3110a8d925b421ee"
GO_CLEARING_SOURCE_DIR = "${WORKDIR}/clearing-source/io4edge-client-go-${PV}"
GO_CLEARING_BINARIES = "${WORKDIR}/io4edge-cli"
GO_CLEARING_GO_VERSION = "go1.17.5"
GO_CLEARING_GO_SHA256 = "3defb9a09bed042403195e872dcbc8c6fae1485963332279668ec52e80a95a2d"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
# SRC_URI_x86-64 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
# SRC_URI_arm = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
# SRC_URI_i586 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
# SRC_URI_i686 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI:aarch64 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"

# SRC_URI[386.md5sum] = "03193f6b4a643704ae2409426822f02e"
# SRC_URI[386.sha256sum] = "8149a2eb4b36f01be90f1ffb28f49e9ffe2d4a2e2038ccef420c9901bd59d1c2"
# SRC_URI[amd64.md5sum] = "c7ef7eabd1167beb53136d1eee30ba36"
# SRC_URI[amd64.sha256sum] = "adf8cb1372510cc2f378bb4d3404c71e858a187c20c52421388217c98caceffe"
SRC_URI[arm64.md5sum] = "812797d05f231106184ff38ac9283266"
SRC_URI[arm64.sha256sum] = "86b90171102605162c46036fe4fed77f6b14ede314864bc970cdfd5570bb2173"
# SRC_URI[arm.md5sum] = "8a89509bda8556290b06635dbf3a6d02"
# SRC_URI[arm.sha256sum] = "f7833c2cbd7be99e9a4fb5722978e0734e15b4bb8e51002f37f9c56bcf6859a2"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/io4edge-cli ${D}${bindir}/io4edge-cli
}

FILES_${PN} += "${bindir}/io4edge-cli"
