inherit go
inherit features_check

DESCRIPTION = "Command Line Interface for Io4Edge Devices"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "go-dep-native"
GO_LINKSHARED = ""
GO_IMPORT = "github.com/ci4rail/io4edge-cli"
SRC_URI = "git://github.com/ci4rail/io4edge-cli;protocol=https;branch=add_mdns_browser"

SRCREV = "fb64f6dd92a3b0590cc51df7b3393614100b0f57"

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