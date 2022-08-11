SUMMARY = "Socketcan support for Io4edge CANL2 devices"
DESCRIPTION = "Gateway between virtual socket CAN and io4edge CAN"
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

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[386.md5sum] = "0aceae9ed58a4b2bdc23c6a74e9aebd5"
SRC_URI[386.sha256sum] = "dda4a57e7121e44e0e65e84a92bdcdc05fef70559c0c51aeae10c3f68e99bd1b"
SRC_URI[amd64.md5sum] = "51aa8d59c3b7757704a058a9c9a99623"
SRC_URI[amd64.sha256sum] = "a4a01407fe5ecd98d87def3fb03d61056bdf8828a632344530e1a2fa893ad462"
SRC_URI[arm64.md5sum] = "598e3a5ef3b13fea5a27d7ed1c1bb73f"
SRC_URI[arm64.sha256sum] = "351fab834d5646998a0e69485872b96450a11786649b77c47daf8d9d72544ad5"
SRC_URI[arm.md5sum] = "23704278ad9fd74e985df9c3eae89bbd"
SRC_URI[arm.sha256sum] = "b771af64841060583a3a601f3231485c4102b0a1d0a59e303fcc9606043879bc"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/${BPN} ${D}${bindir}/${BPN}
}

FILES_${PN} += "${bindir}/${BPN}"
