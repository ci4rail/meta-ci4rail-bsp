SUMMARY = "moducop-core-api-server: REST API Server for moducop devices"
DESCRIPTION = "moducop-core-api-server is a REST API Server that abstracts management functions of moducop devices"
HOMEPAGE = "https://github.com/ci4rail/moducop-core-api-server"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=2b42edef8fa55315f34f2370b4715ca9"

COMPATIBLE_HOST = "(aarch64).*-linux"

SRC_URI:aarch64 = "https://github.com/ci4rail/moducop-core-api-server/releases/download/v${PV}/core-api-server-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI:append = " file://${BPN}.service"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[arm64.md5sum] = "3997408ac9d1b1a56c7b6b4ee9385410"
SRC_URI[arm64.sha256sum] = "1e7dad5df52e2c55f0dec4d0f4de9fa17558a66ff1b36623002d979cd3a8bf33"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${BPN} = "${BPN}.service"
FILES:${PN} += "${systemd_system_unitdir}/${BPN}.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/core-api-server ${D}${bindir}/core-api-server
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system
}

FILES:${PN} += "${bindir}/core-api-server"

inherit systemd
