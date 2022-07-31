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

#SRC_URI[amd64.md5sum] = "8fb49df93b6cd4691a5b20f0b761b250"
#SRC_URI[amd64.sha256sum] = "4b75f26aa1f21dc34e8b96d7ab81f278632d33ed9d72b8849d92e79e16e8b1ca"
#SRC_URI[arm.md5sum] = "d3e440db28f3a0ba8e5bfca4aae27901"
#SRC_URI[arm.sha256sum] = "6d990206911ae06d232be9f23c9f0d314f95109808e2f0a25166b5c370d4d61f"
SRC_URI[arm64.md5sum] = "b9dbd4f24352c33bdddb9d1948134f04"
SRC_URI[arm64.sha256sum] = "978ceb47824264ea2e530946ba8afeabe18e9f10697a3dae9b20521caa3e7fe4"
#SRC_URI[386.md5sum] = "90299e95bd719ed28dd1145282c3a69c"
#SRC_URI[386.sha256sum] = "54999f558c575affbd34201fd67797ecb6094c30674308c864bb0caedec48ba0"

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
