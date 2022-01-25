
CI4RAIL_DATA_PART ?= "/dev/mmcblk0p3"

# Update fstab to include data partition
ROOTFS_POSTPROCESS_COMMAND_append = " ci4rail_update_fstab_file;"
ci4rail_update_fstab_file() {

    mkdir -p ${IMAGE_ROOTFS}/data
    printf "%-20s %-20s %-10s %-21s %-2s %s\n" ${CI4RAIL_DATA_PART} /data ext4 defaults 0 2 >> ${IMAGE_ROOTFS}${sysconfdir}/fstab
}
