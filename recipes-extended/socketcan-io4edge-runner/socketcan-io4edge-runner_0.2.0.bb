SUMMARY = "Socketcan support for Io4edge CANL2 devices"
DESCRIPTION = "Adds a systemservice to scan for io4edge CANL2 devices and start a gateway that connects virtual socketcan with io4edge CAN"
HOMEPAGE = "https://github.com/ci4rail/socketcan-io4edge"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI:x86-64 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI:arm = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI_i586 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_i686 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI:aarch64 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI:append = " file://${BPN}.service"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[386.md5sum] = "1d55a218f93920f27e70d360b819c72f"
SRC_URI[386.sha256sum] = "1a90dd234d1e8ef512c58c193e782c8faa8bbcb7fdbc897fd57b9ed801860c47"
SRC_URI[amd64.md5sum] = "ee741572b874debe9521fb0366c85687"
SRC_URI[amd64.sha256sum] = "29b5231c1f9df46266c1b8c7d71516502e87beff876c853d2569ad8a4dcc2910"
SRC_URI[arm64.md5sum] = "79c388439a0829b7e55f7cafbf5a5f67"
SRC_URI[arm64.sha256sum] = "60ca8fea8e03e741bfae02d97f302e3fbcb3bd99f4f80f2d3a6aacdeb36d657d"
SRC_URI[arm.md5sum] = "09cf09603a2827028f53e2663deebf97"
SRC_URI[arm.sha256sum] = "f6af9190ec4540df79a42c9e94a00f3828cc603c2e39aa92922f133350908b8a"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${BPN} = "${BPN}.service"
FILES:${PN} += "${systemd_system_unitdir}/${BPN}.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/${BPN} ${D}${bindir}/${BPN}
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system
}

FILES:${PN} += "${bindir}/${BPN}"

inherit systemd
