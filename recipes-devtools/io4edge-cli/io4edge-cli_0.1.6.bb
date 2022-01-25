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

SRC_URI[amd64.md5sum] = "f7327004dee43860bb81c499c7c614c2"
SRC_URI[amd64.sha256sum] = "49a4585e6e0330091deded3709f67dc93d74e6bba528ff96e128754ebe767dca"
SRC_URI[arm.md5sum] = "e8672b9af92c8da88992f8f93f3c9283"
SRC_URI[arm.sha256sum] = "807d4e00f65675cb53e788e7300cfff0b81894c4b5993bc7a0886961c1a8e115"
SRC_URI[arm64.md5sum] = "e4afe2b3720b1570ac9a273fa07bd892"
SRC_URI[arm64.sha256sum] = "62be0dd3acc16aacb91f003e7e6f2e6ab054c84daf789c20717177594281ce65"
SRC_URI[386.md5sum] = "03f737ea2e7efa92663b9c84bd3be623"
SRC_URI[386.sha256sum] = "2e3786f85e34f2df437382a4793758d81b1a5531d8b4c7c7d0bc982f48ddf4dd"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/io4edge-cli ${D}${bindir}/io4edge-cli
}

FILES_${PN} += "${bindir}/io4edge-cli"
