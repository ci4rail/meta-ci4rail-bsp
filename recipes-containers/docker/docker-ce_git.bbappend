DOCKER_OVERLAY_LOWER_DIR ?= "/etc/docker"
DOCKER_OVERLAY_UPPER_DIR ?= "/data/docker"
DOCKER_OVERLAY_WORK_DIR ?= "/data/.overlay/docker-work"
DOCKER_OVERLAY_OPTIONS ?= "0  0"

# Update fstab for docker overlay on read-only filesystem for `/etc/docker`
ROOTFS_POSTPROCESS_COMMAND_append = " docker_update_fstab_file;"
docker_update_fstab_file() {
    mkdir -p ${IMAGE_ROOTFS}/data/docker
    mkdir -p ${IMAGE_ROOTFS}/data/.overlay/docker-work

    echo overlay ${DOCKER_OVERLAY_LOWER_DIR} overlay lowerdir=${DOCKER_OVERLAY_LOWER_DIR},upperdir=${DOCKER_OVERLAY_UPPER_DIR},workdir=${DOCKER_OVERLAY_WORK_DIR} ${DOCKER_OVERLAY_OPTIONS} >> ${IMAGE_ROOTFS}${sysconfdir}/fstab
}