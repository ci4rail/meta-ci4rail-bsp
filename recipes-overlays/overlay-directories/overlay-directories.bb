inherit systemd

require overlay-directories.inc

SUMMARY = "Creates and mounts overlay directories for several modules used by meta-ci.os"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://overlay-directories.service"
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
# create docker overlay directories
mkdir -p ${DOCKER_OVERLAY_CONFIG_ROOT_DIR}
mkdir -p ${DOCKER_OVERLAY_CONFIG_UPPER_DIR}
mkdir -p ${DOCKER_OVERLAY_CONFIG_WORK_DIR}
# create iotedge overlay directories
mkdir -p ${IOTEDGE_OVERLAY_CONFIG_ROOT_DIR}
mkdir -p ${IOTEDGE_OVERLAY_CONFIG_UPPER_DIR}
mkdir -p ${IOTEDGE_OVERLAY_CONFIG_WORK_DIR}
# mount overlays
mount -t overlay overlay -o lowerdir=${DOCKER_OVERLAY_CONFIG_LOWER_DIR},upperdir=${DOCKER_OVERLAY_CONFIG_UPPER_DIR},workdir=${DOCKER_OVERLAY_CONFIG_WORK_DIR} ${DOCKER_OVERLAY_CONFIG_LOWER_DIR}
mount -t overlay overlay -o lowerdir=${IOTEDGE_OVERLAY_CONFIG_LOWER_DIR},upperdir=${IOTEDGE_OVERLAY_CONFIG_UPPER_DIR},workdir=${IOTEDGE_OVERLAY_CONFIG_WORK_DIR} ${IOTEDGE_OVERLAY_CONFIG_LOWER_DIR}
EOF
    chmod +x ${D}${CREATE_OVERLAY_DIRECTORIES_SCRIPT}
    install -m 0644 ${WORKDIR}/overlay-directories.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += "${CREATE_OVERLAY_DIRECTORIES_SCRIPT}"
FILES_${PN} += "${systemd_system_unitdir}/overlay-directories.service"

REQUIRED_DISTRO_FEATURES= "systemd"