FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://wrapup.sh.in \
"

do_deploy:append() {
    sed -e "s|@MENDER_STORAGE_DEVICE@|${MENDER_STORAGE_DEVICE}|g" \
        -e "s|@MENDER_DATA_PART_NUMBER@|${MENDER_DATA_PART_NUMBER}|g" \
            ${WORKDIR}/wrapup.sh.in > ${DEPLOYDIR}/wrapup.sh
    chmod 755 ${DEPLOYDIR}/wrapup.sh
}