SUMMARY = "Socketcan support for Io4edge CANL2 devices"
DESCRIPTION = "Adds a systemservice to scan for io4edge CANL2 devices and start a gateway that connects virtual socketcan with io4edge CAN"
HOMEPAGE = "https://github.com/ci4rail/socketcan-io4edge"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI_x86-64 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI_arm = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI_i586 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_i686 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-386.tar.gz;name=386"
SRC_URI_aarch64 = "https://github.com/ci4rail/socketcan-io4edge/releases/download/v${PV}/${BPN}-v${PV}-linux-arm64.tar.gz;name=arm64"
SRC_URI_append = " file://${BPN}.service"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[386.md5sum] = "484207155c90f39b5438e1ed02a3f988"
SRC_URI[386.sha256sum] = "1eea0b1a5b19335f9de51a2b4fa8144cb31b9ca4daeafa23779863bf40f22d8a"
SRC_URI[amd64.md5sum] = "7c1bb3d12a208765c5c14e16c04ffef4"
SRC_URI[amd64.sha256sum] = "b2af30f98da11127335989fd82021eb8e430652fb3bcf2652257456c9daede19"
SRC_URI[arm64.md5sum] = "60867d8310cfa33c180a5e7c41bbe645"
SRC_URI[arm64.sha256sum] = "d3061215865f1888f5cae1e8123baaa420778b9830c9e4b04f2cf94fb91d82c4"
SRC_URI[arm.md5sum] = "1432fc1e0b638b784b3a552c243f02bc"
SRC_URI[arm.sha256sum] = "ed40c7cd712c46f62e466227fa669a012abfa57db6147f00fa3911e4d79abd1d"

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
