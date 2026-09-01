SUMMARY = "Mender integration for Core OS customization"
DESCRIPTION = "Mender Update Module, inventory script, and installation adapter for os-customization"
HOMEPAGE = "https://github.com/ci4rail/yocto-os-customization"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d96591d67b8839e9adc28ed53a13beab"

SRC_URI = "git://github.com/ci4rail/yocto-os-customization.git;protocol=https;branch=preinit-compat-check "
SRCREV = "c8c2d14226f4ef5d386eb9b9c02215b4203a6055"

PV = "1.0+git"
S = "${WORKDIR}/git"

inherit allarch

RDEPENDS:${PN} += "os-customization mender-update"

do_install() {
    install -d ${D}${libexecdir}
    install -m 0755 ${S}/libexec/os-customization-mender-install \
        ${D}${libexecdir}/os-customization-mender-install

    install -d ${D}${datadir}/mender/modules/v3
    install -m 0755 ${S}/mender/modules/os-customization \
        ${D}${datadir}/mender/modules/v3/os-customization

    install -d ${D}${datadir}/mender/inventory
    install -m 0755 ${S}/mender/inventory/os-customization \
        ${D}${datadir}/mender/inventory/os-customization
}

FILES:${PN} += "\
    ${datadir}/mender/modules/v3/os-customization \
    ${datadir}/mender/inventory/os-customization \
"
