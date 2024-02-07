FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-set-AUTOCONNECT_RESET_RETRIES_TIMER-to-30s.patch\
    file://0002-hack-for-netbird-don-t-change-wt0-device-listen-port.patch\
    "

