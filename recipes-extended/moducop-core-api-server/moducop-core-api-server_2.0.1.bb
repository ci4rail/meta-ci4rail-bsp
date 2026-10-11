SUMMARY = "moducop-core-api-server: REST API Server for moducop devices"
DESCRIPTION = "moducop-core-api-server is a REST API Server that abstracts management functions of moducop devices"
HOMEPAGE = "https://github.com/ci4rail/moducop-core-api-server"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=2b42edef8fa55315f34f2370b4715ca9"

inherit go-prebuilt-clearing

SRC_URI[clearing_source.sha256sum] = "ef59acbf21cd4fea30203c36f654f964e8e2d269162b80a5e4d45ebbeef3eb7c"
GO_CLEARING_SOURCE_DIR = "${WORKDIR}/clearing-source/moducop-core-api-server-${PV}"
GO_CLEARING_BINARIES = "${WORKDIR}/core-api-server"
GO_CLEARING_GO_VERSION = "go1.26.1"
GO_CLEARING_GO_SHA256 = "3172293d04b209dc1144698e7ba13f0477f6ba8c5ffd0be66c20fdbc9785dfbb"

COMPATIBLE_HOST = "(aarch64).*-linux"

SRC_URI:aarch64 = "https://github.com/ci4rail/moducop-core-api-server/releases/download/v${PV}/core-api-server-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI:append = " https://github.com/ci4rail/moducop-core-api-server/archive/refs/tags/v${PV}.tar.gz;name=clearing_source;downloadfilename=${BPN}-${PV}-source.tar.gz;subdir=clearing-source file://${BPN}.service"

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
