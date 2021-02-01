DESCRIPTION = "KYT Device Status Service"
GO_IMPORT = "github.com/ci4rail/kyt/device-state-service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://github.com/ci4rail/kyt/;protocol=https;branch=feature/deviceStateServcie;subpath=device-state-service;destsuffix=${BPN}-${PV}/src/${GO_IMPORT} \
"
SRCREV = "17b80cf6ef321a8428c8ac3acc5eaf30f0076304"

inherit go

COMPATIBLE_MACHINE = "moducop-cpu01"

# although go dep is deprecated, its the easiest way in Yocto to manage dependencies in go programs
DEPENDS += "go-native go-dep-native"
RDEPENDS_${PN}-staticdev += "bash"

# This will prevent -builmdmode=shared and do a normal go build instead of linking against a standard library
GO_LINKSHARED = ""

do_compile_prepend() {
    ( cd ${WORKDIR}/build/src/${GO_IMPORT} && \
        rm -f Gopkg.toml Gopkg.lock && \
        dep init && dep ensure -v )
}




