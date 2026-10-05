# Create an OTA Artifact for the paired FIT/dm-verity rootfs Update Module.

do_image_mender_bootfit_rootfs[depends] += "mender-artifact-native:do_populate_sysroot"
do_image_mender_bootfit_rootfs[recrdeptask] += "do_deploy"

CI4RAIL_OTA_PROTECTION ?= "0"
CI4RAIL_OTA_AZURE_KEY_ID ?= ""
CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY ?= ""

python __anonymous() {
    if d.getVar('CI4RAIL_OTA_PROTECTION') != '1':
        return
    local_key = d.getVar('MENDER_ARTIFACT_SIGNING_KEY')
    azure_key = d.getVar('CI4RAIL_OTA_AZURE_KEY_ID')
    if bool(local_key) == bool(azure_key):
        bb.fatal('Protected OTA requires exactly one of MENDER_ARTIFACT_SIGNING_KEY or CI4RAIL_OTA_AZURE_KEY_ID')
    if azure_key:
        import re
        match = re.fullmatch(r'https://([a-zA-Z0-9-]+)\.vault\.azure\.net/keys/([a-zA-Z0-9-]+)(?:/([a-zA-Z0-9-]+))?', azure_key)
        if not match:
            bb.fatal('CI4RAIL_OTA_AZURE_KEY_ID must be an Azure Key Vault key URL')
        d.setVar('KEY_VAULT_NAME', match.group(1))
        d.setVar('CI4RAIL_OTA_AZURE_KEY_NAME', match.group(2))
        d.setVar('KEY_VAULT_KEY_VERSION', match.group(3) or '')
        for name in ('KEY_VAULT_NAME', 'KEY_VAULT_KEY_VERSION', 'AZURE_CLIENT_ID',
                     'AZURE_TENANT_ID', 'AZURE_FEDERATED_TOKEN_FILE', 'AZURE_AUTHORITY_HOST'):
            d.setVarFlag(name, 'export', '1')
        d.setVarFlag('do_image_mender_bootfit_rootfs', 'network', '1')
}

CI4RAIL_OTA_AZURE_KEY_NAME ?= ""

CI4RAIL_MENDER_BOOTFIT_ARTIFACT_TYPE ?= "bootfit-rootfs"
CI4RAIL_MENDER_BOOTFIT_ARTIFACT_FIT ?= "${DEPLOY_DIR_IMAGE}/${TORADEX_MENDER_BOOTFIT_FILENAME}"
CI4RAIL_MENDER_BOOTFIT_ARTIFACT_ROOTFS ?= "${IMGDEPLOYDIR}/${ARTIFACTIMG_NAME}.${ARTIFACTIMG_FSTYPE}"

ci4rail_mender_add_arguments() {
    local flag="$1"
    shift
    for argument in "$@"; do
        extra_args="${extra_args} ${flag} ${argument}"
    done
}

IMAGE_CMD:mender-bootfit-rootfs () {
    if [ -z "${MENDER_ARTIFACT_NAME}" ]; then
        bbfatal "Need to define MENDER_ARTIFACT_NAME variable."
    fi
    if [ -z "${MENDER_DEVICE_TYPES_COMPATIBLE}" ]; then
        bbfatal "MENDER_DEVICE_TYPES_COMPATIBLE variable cannot be empty."
    fi
    if [ ! -f "${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_FIT}" ]; then
        bbfatal "Signed boot FIT payload is missing: ${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_FIT}"
    fi
    if [ ! -f "${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_ROOTFS}" ]; then
        bbfatal "dm-verity rootfs payload is missing: ${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_ROOTFS}"
    fi

    rootfs_size=$(stat -Lc %s "${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_ROOTFS}")
    calc_rootfs_size=$(expr ${MENDER_CALC_ROOTFS_SIZE} \* 1024)
    if [ "${rootfs_size}" -gt "${calc_rootfs_size}" ]; then
        bbfatal "Size of rootfs is greater than the calculated partition space (${rootfs_size} > ${calc_rootfs_size})."
    fi

    extra_args=""
    for dev in ${MENDER_DEVICE_TYPES_COMPATIBLE}; do
        ci4rail_mender_add_arguments "-t" "${dev}"
    done

    if [ -n "${MENDER_ARTIFACT_SIGNING_KEY}" ]; then
        ci4rail_mender_add_arguments "-k" "${MENDER_ARTIFACT_SIGNING_KEY}"
    fi
    if [ -d "${DEPLOY_DIR_IMAGE}/mender-state-scripts" ]; then
        ci4rail_mender_add_arguments "-s" "${DEPLOY_DIR_IMAGE}/mender-state-scripts"
    fi
    if [ -n "${MENDER_ARTIFACT_NAME_DEPENDS}" ]; then
        ci4rail_mender_add_arguments "--artifact-name-depends" ${MENDER_ARTIFACT_NAME_DEPENDS}
    fi
    if [ -n "${MENDER_ARTIFACT_PROVIDES}" ]; then
        ci4rail_mender_add_arguments "--provides" ${MENDER_ARTIFACT_PROVIDES}
    fi
    if [ -n "${MENDER_ARTIFACT_PROVIDES_GROUP}" ]; then
        ci4rail_mender_add_arguments "--provides-group" ${MENDER_ARTIFACT_PROVIDES_GROUP}
    fi
    if [ -n "${MENDER_ARTIFACT_DEPENDS}" ]; then
        ci4rail_mender_add_arguments "--depends" ${MENDER_ARTIFACT_DEPENDS}
    fi
    if [ -n "${MENDER_ARTIFACT_DEPENDS_GROUPS}" ]; then
        ci4rail_mender_add_arguments "--depends-groups" ${MENDER_ARTIFACT_DEPENDS_GROUPS}
    fi

    mender-artifact write module-image \
        -n "${MENDER_ARTIFACT_NAME}" \
        ${extra_args} \
        -T "${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_TYPE}" \
        -f "${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_FIT}" \
        -f "${CI4RAIL_MENDER_BOOTFIT_ARTIFACT_ROOTFS}" \
        ${MENDER_ARTIFACT_EXTRA_ARGS} \
        -o "${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.mender"

    if [ "${CI4RAIL_OTA_PROTECTION}" = "1" ]; then
        if [ -n "${CI4RAIL_OTA_AZURE_KEY_NAME}" ]; then
            mender-artifact sign --azure-key "${CI4RAIL_OTA_AZURE_KEY_NAME}" \
                "${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.mender"
        fi
        mender-artifact validate -k "${CI4RAIL_OTA_ARTIFACT_PUBLIC_KEY}" \
            "${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.mender"
    fi

    ln -sf "${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.mender" \
        "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.mender"
}

IMAGE_CMD:mender-bootfit-rootfs[vardepsexclude] += "IMAGE_ID"
IMAGE_TYPEDEP:mender-bootfit-rootfs:append = " ${ARTIFACTIMG_FSTYPE}"
