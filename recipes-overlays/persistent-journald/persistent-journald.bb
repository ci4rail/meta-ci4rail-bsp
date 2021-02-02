inherit systemd

require persistent-journald.inc

SUMMARY = "Creates and mounts overlay directories for several modules used by meta-ci.os"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://persistent-journald.service"
SRC_URI += "file://journald.conf"

SYSTEMD_SERVICE_${PN} = "persistent-journald.service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# ${bindir} expands to `/usr/bin/`
JOURNALD_FLUSH_PERSISTENT_SCRIPT ?= "${bindir}/persistent-journald.sh"

SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${sysconfdir}/systemd
    cat <<EOF > ${D}${JOURNALD_FLUSH_PERSISTENT_SCRIPT}
#!/bin/sh
# create journald overlay directories
mkdir -p ${JOURNAL_OVERLAY_RUN_ROOT_DIR}
mkdir -p ${JOURNAL_OVERLAY_RUN_LOWER_DIR}
mkdir -p ${JOURNAL_OVERLAY_RUN_UPPER_DIR}
mkdir -p ${JOURNAL_OVERLAY_RUN_WORK_DIR}

# mount overlay
mount -t overlay overlay -o lowerdir=${JOURNAL_OVERLAY_RUN_LOWER_DIR},upperdir=${JOURNAL_OVERLAY_RUN_UPPER_DIR},workdir=${JOURNAL_OVERLAY_RUN_WORK_DIR} ${JOURNAL_OVERLAY_RUN_LOWER_DIR}

# flush to copy from volatile to persistent
journalctl --flush
EOF
    install -m 0644 ${WORKDIR}/journald.conf ${D}${sysconfdir}/systemd
    chmod +x ${D}${JOURNALD_FLUSH_PERSISTENT_SCRIPT}
    install -m 0644 ${WORKDIR}/persistent-journald.service ${D}${systemd_system_unitdir}
    
}

FILES_${PN} += "${sysconfdir}/systemd/journald.conf"
FILES_${PN} += "${CREATE_OVERLAY_DIRECTORIES_SCRIPT}"
FILES_${PN} += "${systemd_system_unitdir}/persistent-journald.service"

REQUIRED_DISTRO_FEATURES= "systemd"
