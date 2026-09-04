SUMMARY = "Core OS customization lifecycle support"
DESCRIPTION = "A/B-managed customer configuration for /etc on an immutable root filesystem"
HOMEPAGE = "https://github.com/ci4rail/yocto-os-customization"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d96591d67b8839e9adc28ed53a13beab"

SRC_URI = "git://github.com/ci4rail/yocto-os-customization.git;protocol=https;branch=main "
SRCREV = "c66e60fac9c977934b29f074453dce5e0c7830d1"

PV = "1.0+git"
S = "${WORKDIR}/git"

inherit allarch systemd

OS_CUSTOMIZATION_DATA_DEVICE ?= "${MENDER_DATA_PART}"

RDEPENDS:${PN} += "\
    python3-core \
    python3-fcntl \
    python3-io \
    python3-json \
    python3-shell \
    systemd \
"

SYSTEMD_SERVICE:${PN} = "\
    os-customization-check.timer \
    os-customization-factory-reset.service \
"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/bin/os-customization-set \
        ${D}${bindir}/os-customization-set

    install -d ${D}${base_sbindir}
    install -m 0755 ${S}/sbin/os-customization-preinit.sh \
        ${D}${base_sbindir}/os-customization-preinit
    sed -i -e 's|^DATA_DEVICE=.*|DATA_DEVICE=${OS_CUSTOMIZATION_DATA_DEVICE}|' \
        ${D}${base_sbindir}/os-customization-preinit

    install -d ${D}${libexecdir}
    install -m 0755 ${S}/libexec/os-customization-check \
        ${D}${libexecdir}/os-customization-check

    install -d ${D}${libdir}/os-customization/python/os_customization
    install -m 0644 ${S}/python/os_customization/__init__.py \
        ${D}${libdir}/os-customization/python/os_customization/__init__.py
    install -m 0644 ${S}/python/os_customization/manager.py \
        ${D}${libdir}/os-customization/python/os_customization/manager.py

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/systemd/os-customization-check.service \
        ${D}${systemd_system_unitdir}/os-customization-check.service
    install -m 0644 ${S}/systemd/os-customization-check.timer \
        ${D}${systemd_system_unitdir}/os-customization-check.timer
    install -m 0644 ${S}/systemd/os-customization-factory-reset.service \
        ${D}${systemd_system_unitdir}/os-customization-factory-reset.service
}

FILES:${PN} += "\
    ${libdir}/os-customization \
    ${systemd_system_unitdir}/os-customization-check.service \
"

pkg_postinst:${PN}() {
    if [ -z "$D" ]; then
        echo "Installing ${PN} on a running system is not supported" >&2
        exit 1
    fi

    init_path="$D${base_sbindir}/init"
    real_init_path="$D${base_sbindir}/init.real"

    if [ -e "$real_init_path" ] || [ -L "$real_init_path" ]; then
        if [ "$(readlink "$init_path" 2>/dev/null || true)" != "os-customization-preinit" ]; then
            echo "Refusing to replace $init_path: $real_init_path already exists" >&2
            exit 1
        fi
    elif [ -e "$init_path" ] || [ -L "$init_path" ]; then
        mv "$init_path" "$real_init_path"
    else
        echo "Cannot preserve the original init: $init_path does not exist" >&2
        exit 1
    fi

    ln -snf os-customization-preinit "$init_path"
}
