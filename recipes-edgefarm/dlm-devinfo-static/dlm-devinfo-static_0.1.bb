inherit systemd
inherit go
inherit features_check

DESCRIPTION = "Edgefarm Device Info Static Service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "go-dep-native"
GO_LINKSHARED = ""
GO_IMPORT = "github.com/ci4rail/dlm-devinfo-static"
SRC_URI = "git://github.com/ci4rail/dlm-devinfo-static;protocol=https;branch=main"
SRC_URI += "file://dlm-devinfo-static.service"

SRCREV = "${AUTOREV}"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "dlm-devinfo-static.service"
REQUIRED_DISTRO_FEATURES= "systemd"

do_compile() {
    mkdir -p ${WORKDIR}/build/bin
    cd ${WORKDIR}/build/src/${GO_IMPORT}/src && BIN_DIR=${WORKDIR}/build/bin make
    # this is a hack that `do_rm_work` and `do_clean` can do their work
    chmod -R 777 ${WORKDIR}/build/pkg
}

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/dlm-devinfo-static.service ${D}${systemd_system_unitdir}

    install -d ${D}${bindir}
    install -m 0755 "${WORKDIR}/build/bin/dlm-devinfo-static" "${D}/${bindir}/dlm-devinfo-static"
}

FILES_${PN} += "${systemd_system_unitdir}/dlm-devinfo-static.service"