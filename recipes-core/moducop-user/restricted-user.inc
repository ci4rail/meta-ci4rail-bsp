# Common part of the restricted administrator accounts (moducop, ci4rail).
# The including recipe sets RESTRICTED_USER before including this file.
#
# The account is created without a usable password and without SSH key.
# Key and password hash are provided by an os-customization:
#   /etc/ssh/authorized_keys/${RESTRICTED_USER}
#   /etc/ci4rail/user-passwords/${RESTRICTED_USER}
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit useradd allarch

S = "${WORKDIR}"

RESTRICTED_USER_HOME = "/home/${RESTRICTED_USER}"

USERADD_PACKAGES = "${PN}"
# '*' is no valid password hash but, unlike '!', not a locked account: SSH key
# login works once a customization provides a key. The password is set at boot
# from /etc/ci4rail/user-passwords/<user> when a customization provides it.
USERADD_PARAM:${PN} = "-M -d ${RESTRICTED_USER_HOME} -s /bin/sh -p '*' ${RESTRICTED_USER}"

do_install() {
    install -d -m 0700 ${D}${RESTRICTED_USER_HOME}
    install -d -m 0700 ${D}${RESTRICTED_USER_HOME}/.ssh
    chown ${RESTRICTED_USER}:${RESTRICTED_USER} ${D}${RESTRICTED_USER_HOME} ${D}${RESTRICTED_USER_HOME}/.ssh
    # The home directory is on the read-only rootfs and dropbear only reads
    # ~/.ssh/authorized_keys. Point it to the os-customization managed /etc.
    ln -s /etc/ssh/authorized_keys/${RESTRICTED_USER} ${D}${RESTRICTED_USER_HOME}/.ssh/authorized_keys
    chown -h ${RESTRICTED_USER}:${RESTRICTED_USER} ${D}${RESTRICTED_USER_HOME}/.ssh/authorized_keys
}

FILES:${PN} = "${RESTRICTED_USER_HOME}"

RDEPENDS:${PN} = "sudo"
