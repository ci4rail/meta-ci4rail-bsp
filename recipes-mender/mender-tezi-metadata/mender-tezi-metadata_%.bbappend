#
# This adds a specific wrapup.sh script to the mender-tezi-metadata package
# to extend the data partition to the full size of the eMMC already during
# TEZI runtime. 
#
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://wrapup.sh.in \
"

do_deploy:append() {
    sed -e "s|@MENDER_STORAGE_DEVICE@|${MENDER_STORAGE_DEVICE}|g" \
        -e "s|@MENDER_DATA_PART_NUMBER@|${MENDER_DATA_PART_NUMBER}|g" \
            ${WORKDIR}/wrapup.sh.in > ${DEPLOYDIR}/mender-tezi-metadata/wrapup.sh
    chmod 755 ${DEPLOYDIR}/mender-tezi-metadata/wrapup.sh
}
