SUMMARY = "Tool to read out inventory from Ci4Rail Board EEPROMs"
DESCRIPTION = "Tool to read out inventory from Ci4Rail Board EEPROMs"
HOMEPAGE = "https://github.com/ci4rail/ee-inv-cli"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

COMPATIBLE_HOST = "(x86_64|aarch64|arm).*-linux"

# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI:x86-64 = "https://github.com/ci4rail/ee-inv-cli/releases/download/v${PV}/ee-inv-v${PV}-linux-amd64.tar.gz;name=amd64"
SRC_URI:arm = "https://github.com/ci4rail/ee-inv-cli/releases/download/v${PV}/ee-inv-v${PV}-linux-arm.tar.gz;name=arm"
SRC_URI:aarch64 = "https://github.com/ci4rail/ee-inv-cli/releases/download/v${PV}/ee-inv-v${PV}-linux-arm64.tar.gz;name=arm64"

# Use github-release-checksums.sh script in yoct-images repo to update
SRC_URI[amd64.md5sum] = "b38ad1c091945cffd14c59da7e169641"
SRC_URI[amd64.sha256sum] = "10907e4cbdc3a0a645abade15dd0e637092210a943371d0b507d12f93b8b7e29"
SRC_URI[arm64.md5sum] = "8e366a4d7bd49fc5ab88bac7a4e0a545"
SRC_URI[arm64.sha256sum] = "9f49048cee8a97fde4e0f5e599bdb8856587452c305b215485e5a06e933e5e08"
SRC_URI[arm.md5sum] = "fe3a780601a2cbfce8cf7478250ad5f1"
SRC_URI[arm.sha256sum] = "e5f3ad11fc0da33babebc6854d709bfe712aec5375ecbe331b4e6701c046edbf"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/ee-inv ${D}${bindir}/
}

FILES:${PN} += "${bindir}/ee-inv"
