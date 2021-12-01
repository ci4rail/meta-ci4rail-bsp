inherit systemd
inherit features_check
require overlay-directories.inc

SUMMARY = "Creates and mounts overlay directories"
DESCRIPTION = "Creates and mounts overlay directories for several modules used by meta-ci.os.\
This is requied as the FS is read only."
HOMEPAGE = "https://ci4rail.com"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = " file://overlay-directories.service"
SYSTEMD_SERVICE_${PN} = "overlay-directories.service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# ${bindir} expands to `/usr/bin/`
CREATE_OVERLAY_DIRECTORIES_SCRIPT ?= "${bindir}/overlay-directories.sh"

SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}

    cat <<EOF > ${D}${CREATE_OVERLAY_DIRECTORIES_SCRIPT}
#!/bin/sh
# etc
mkdir -p ${ETC_OVERLAY_CONFIG_ROOT_DIR}
mkdir -p ${ETC_OVERLAY_CONFIG_UPPER_DIR}
mkdir -p ${ETC_OVERLAY_CONFIG_WORK_DIR}

# mount overlays
mount -t overlay overlay -o lowerdir=${ETC_OVERLAY_CONFIG_LOWER_DIR},upperdir=${ETC_OVERLAY_CONFIG_UPPER_DIR},workdir=${ETC_OVERLAY_CONFIG_WORK_DIR} ${ETC_OVERLAY_CONFIG_LOWER_DIR}

# copy machine-id from /run (which is bind mounted to /etc/machine-id)
cp /run/machine-id /etc/machine-id
EOF
    chmod +x ${D}${CREATE_OVERLAY_DIRECTORIES_SCRIPT}
    install -m 0644 ${WORKDIR}/overlay-directories.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += "${CREATE_OVERLAY_DIRECTORIES_SCRIPT}"
FILES_${PN} += "${systemd_system_unitdir}/overlay-directories.service"

REQUIRED_DISTRO_FEATURES= "systemd"
