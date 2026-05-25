# The upstream recipe uses a wildcard here, which breaks once the build has
# more than one verity env file in staging. ci4rail-base-image exports
# IMAGE_BASENAME as Standard-Image for this product, so pick it explicitly.
CI4RAIL_DM_VERITY_IMAGE_BASENAME ??= "Standard-Image"

deploy_verity_hash() {
    install -D -m 0644 \
        ${STAGING_VERITY_DIR}/${CI4RAIL_DM_VERITY_IMAGE_BASENAME}.${DM_VERITY_IMAGE_TYPE}.verity.env \
        ${IMAGE_ROOTFS}${datadir}/misc/dm-verity.env
}
