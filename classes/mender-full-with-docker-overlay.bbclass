# `mender-full` because it already updates /etc/fstab. Therefore we need to hook on that.
inherit mender-full

DOCKER_OVERLAY_CONFIG_LOWER_DIR ?= "/etc/docker"
DOCKER_OVERLAY_CONFIG_UPPER_DIR ?= "/data/docker/etc/docker"
DOCKER_OVERLAY_CONFIG_WORK_DIR ?= "/data/.overlay/docker-work/etc/docker"
DOCKER_OVERLAY_CONFIG_OPTIONS ?= "0  0"

DOCKER_OVERLAY_RUN_LOWER_DIR ?= "/var/lib/docker"
DOCKER_OVERLAY_RUN_UPPER_DIR ?= "/data/docker/var/lib/docker"
DOCKER_OVERLAY_RUN_WORK_DIR ?= "/data/.overlay/docker-work/var/lib/docker"
DOCKER_OVERLAY_RUN_OPTIONS ?= "0  0"

# Update fstab for docker
ROOTFS_POSTPROCESS_COMMAND_append = " docker_update_fstab_file;"
docker_update_fstab_file() {
    mkdir -p ${IMAGE_ROOTFS}${DOCKER_OVERLAY_CONFIG_UPPER_DIR}
    mkdir -p ${IMAGE_ROOTFS}${DOCKER_OVERLAY_CONFIG_WORK_DIR}
    mkdir -p ${IMAGE_ROOTFS}${DOCKER_OVERLAY_RUN_UPPER_DIR}
    mkdir -p ${IMAGE_ROOTFS}${DOCKER_OVERLAY_RUN_WORK_DIR}
    printf "\n# Overlays for docker specific directories that need to be writable and persistant\n" >> ${IMAGE_ROOTFS}${sysconfdir}/fstab
    echo overlay ${DOCKER_OVERLAY_CONFIG_LOWER_DIR} overlay lowerdir=${DOCKER_OVERLAY_CONFIG_LOWER_DIR},upperdir=${DOCKER_OVERLAY_CONFIG_UPPER_DIR},workdir=${DOCKER_OVERLAY_CONFIG_WORK_DIR} ${DOCKER_OVERLAY_CONFIG_OPTIONS} >> ${IMAGE_ROOTFS}${sysconfdir}/fstab
    echo overlay ${DOCKER_OVERLAY_RUN_LOWER_DIR} overlay lowerdir=${DOCKER_OVERLAY_RUN_LOWER_DIR},upperdir=${DOCKER_OVERLAY_RUN_UPPER_DIR},workdir=${DOCKER_OVERLAY_RUN_WORK_DIR} ${DOCKER_OVERLAY_RUN_OPTIONS} >> ${IMAGE_ROOTFS}${sysconfdir}/fstab
}
