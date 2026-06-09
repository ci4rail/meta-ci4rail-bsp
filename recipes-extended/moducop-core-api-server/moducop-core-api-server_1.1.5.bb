SUMMARY = "moducop-core-api-server: REST API Server for moducop devices"
DESCRIPTION = "moducop-core-api-server is a REST API Server that abstracts management functions of moducop devices"
HOMEPAGE = "https://github.com/ci4rail/moducop-core-api-server"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(aarch64).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI:aarch64 = "https://github.com/ci4rail/moducop-core-api-server/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI:append = " file://${BPN}.service"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[arm64.md5sum] = "79c388439a0829b7e55f7cafbf5a5f67"
SRC_URI[arm64.sha256sum] = "60ca8fea8e03e741bfae02d97f302e3fbcb3bd99f4f80f2d3a6aacdeb36d657d"

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
