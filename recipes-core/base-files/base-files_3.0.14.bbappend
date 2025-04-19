FILESEXTRAPATHS:prepend := "${THISDIR}/base-files:"

do_install:append () {
    # create mount point for sdcard
    
    install -d ${D}/run/media/sdcard
}