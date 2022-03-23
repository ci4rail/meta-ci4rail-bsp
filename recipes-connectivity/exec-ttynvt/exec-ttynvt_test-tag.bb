SUMMARY = "ttyNVT Executer"
DESCRIPTION = ""
HOMEPAGE = "https://github.com/ci4rail/io4edge-client-go"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI_x86-64 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI_arm = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI_i586 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_i686 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_aarch64 = "https://github.com/ci4rail/io4edge-client-go/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI_append = " file://${BPN}.service"

SRC_URI[amd64.md5sum] = "fde4d0828267da104c456e16ee113849"
SRC_URI[amd64.sha256sum] = "75bf07642a5447a2703345d24381853d6e211d75d6b8eded6577fd8bd470a7ec"
SRC_URI[arm.md5sum] = "6c9424efa43902874fb2d44838b10519"
SRC_URI[arm.sha256sum] = "55fed4f0d4481f09b42eae26a1c7f0442578c9586a44703c78fa08f91aa8f463"
SRC_URI[arm64.md5sum] = "54e6b9569637461682e8566370b4d249"
SRC_URI[arm64.sha256sum] = "6ff34a51f6516c6f5f94745b9506af7a61a2bb7b8cee3002f47823880a4c467c"
SRC_URI[386.md5sum] = "6a9fa8ecf02306a94b7f5f82e52fabc1"
SRC_URI[386.sha256sum] = "c27c95be6981f6f3e981dceb1197d209552ba2d355788220370451ab62cf6da7"

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
