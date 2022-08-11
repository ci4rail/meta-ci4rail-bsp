SUMMARY = "ttyNVT Runner"
DESCRIPTION = ""
HOMEPAGE = "https://github.com/ci4rail/ttynvt-runner"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI_x86-64 = "https://github.com/ci4rail/ttynvt-runner/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI_arm = "https://github.com/ci4rail/ttynvt-runner/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI_i586 = "https://github.com/ci4rail/ttynvt-runner/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_i686 = "https://github.com/ci4rail/ttynvt-runner/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_aarch64 = "https://github.com/ci4rail/ttynvt-runner/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI_append = " file://${BPN}.service"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[386.md5sum] = "5558db692f2136500cfd6f35b4d2c216"
SRC_URI[386.sha256sum] = "880722caf282d45e28e63ca082fbf3736d658c8cc60b335ba7eff64322ea2897"
SRC_URI[amd64.md5sum] = "8b7ac6ceeb87e025aad46b7cf4cecc2a"
SRC_URI[amd64.sha256sum] = "139c3f92f41700bae94d7db92e5015fd37105919b6c7b334e080d88599253fa0"
SRC_URI[arm64.md5sum] = "6535f106d710d24b1678ced85b88edfd"
SRC_URI[arm64.sha256sum] = "90b994fcc398630e14f4c6e311c8636b8854d071b5ab31beade3380a84ad4713"
SRC_URI[arm.md5sum] = "daed01ba5b63884d8faaf9d1ccb1db14"
SRC_URI[arm.sha256sum] = "f62eede02acca92b96f08ce9ae5b734ecd48f84fe3c9d5efaf81f041a8050aba"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${BPN} = "${BPN}.service"
FILES_${PN} += "${systemd_system_unitdir}/${BPN}.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/${BPN} ${D}${bindir}/${BPN}
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system
}

FILES_${PN} += "${bindir}/${BPN}"

inherit systemd
