inherit go
inherit features_check


SUMMARY = "Command Line Interface for Io4Edge Devices"
DESCRIPTION = "Command line tool to manage io4edge devices, e.g. identify the currently running firmware, load new firmware, etc."
HOMEPAGE = "https://github.com/ci4rail/io4edge-cli"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "go-dep-native"
GO_LINKSHARED = ""
GO_IMPORT = "github.com/ci4rail/io4edge-cli"
SRC_URI = "git://github.com/ci4rail/io4edge-cli;protocol=https;branch=main"

SRCREV = "0250ed69514a4aef308bbc7b7f1d7565f67c5819"

do_compile() {
    mkdir -p ${WORKDIR}/build/bin
    cd ${WORKDIR}/build/src/${GO_IMPORT}/cmd/io4edge-cli && BIN_DIR=${WORKDIR}/build/bin make
    # this is a hack that `do_rm_work` and `do_clean` can do their work
    chmod -R 777 ${WORKDIR}/build/pkg
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 "${WORKDIR}/build/bin/io4edge-cli" "${D}/${bindir}/io4edge-cli"
}

FILES_${PN} += "${bindir}/io4edge-cli"
