DESCRIPTION = "KYT Device Info Static Service"
GO_IMPORT = "github.com/ci4rail/kyt/kyt-dlm-devinfo-static"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://github.com/ci4rail/kyt/;protocol=https;branch=feature/dlm-devinfo-iotdev;subpath=kyt-dlm-devinfo-static;destsuffix=${BPN}-${PV}/src/${GO_IMPORT} \
"
SRCREV = "1e500d2b8e1c1bbaad730f9fb0af5d610d9a05a3"

inherit go

#COMPATIBLE_MACHINE = "moducop-cpu01"

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



inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "${BPN}.service"

SRC_URI_append = " file://${BPN}.service "
FILES_${PN} += "${systemd_unitdir}/system/${BPN}.service"

do_install_append() {
  install -d ${D}/${systemd_unitdir}/system
  install -m 0644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system
}
