DOCKER_OVERLAY_CONFIG_ROOT_DIR ?= "/data"
DOCKER_OVERLAY_CONFIG_LOWER_DIR ?= "/etc/docker"
DOCKER_OVERLAY_CONFIG_UPPER_DIR ?= "/data/docker/etc/docker"
DOCKER_OVERLAY_CONFIG_WORK_DIR ?= "/data/.overlay/docker-work/etc/docker"

IOTEDGE_OVERLAY_CONFIG_ROOT_DIR ?= "/data"
IOTEDGE_OVERLAY_CONFIG_LOWER_DIR ?= "/etc/iotedge"
IOTEDGE_OVERLAY_CONFIG_UPPER_DIR ?= "/data/iotedge/etc/iotedge"
IOTEDGE_OVERLAY_CONFIG_WORK_DIR ?= "/data/.overlay/iotedge-work/etc/iotedge"

IOTEDGE_OVERLAY_RUN_ROOT_DIR ?= "/data"
IOTEDGE_OVERLAY_RUN_LOWER_DIR ?= "/var/lib/iotedge"
IOTEDGE_OVERLAY_RUN_UPPER_DIR ?= "/data/iotedge/var/lib/iotedge"
IOTEDGE_OVERLAY_RUN_WORK_DIR ?= "/data/.overlay/iotedge-work/var/lib/iotedge"