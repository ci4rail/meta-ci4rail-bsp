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
SRC_URI[386.md5sum] = "0b9014c30a00f83eaa46af40fd0d06f5"
SRC_URI[386.sha256sum] = "c9b830d5ace1d1abdf5245f6bf3a8ad1b6ff8fab068ce500d5bd594703a82c3d"
SRC_URI[amd64.md5sum] = "cc5f5c104d6fbc4cdd850374995c9e53"
SRC_URI[amd64.sha256sum] = "95a2c51d4a3648b758e528232e3ad43944e8c83c83800883e91fd298fb3d46ef"
SRC_URI[arm64.md5sum] = "4c453673d33fb427e3c6025839f9fbf2"
SRC_URI[arm64.sha256sum] = "99ab31b9a44ec47ac81875107b35159f7e89663c24e3e91bc7b3b009b0491487"
SRC_URI[arm.md5sum] = "d3c40236978c220bd8dc15aece066404"
SRC_URI[arm.sha256sum] = "5cc551560d38a1565b3e5d43fc88eb687c17731418c274efb70b45165f9ec75d"

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
