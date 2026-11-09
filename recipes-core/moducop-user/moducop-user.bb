SUMMARY = "Restricted administrator account moducop"
DESCRIPTION = "Creates the account moducop without a usable password. Login, \
sudo rights and the password are provided by an os-customization, so the \
account is inert in images without such a customization."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit useradd allarch

S = "${WORKDIR}"

MODUCOP_HOME = "/home/moducop"

USERADD_PACKAGES = "${PN}"
# '*' is no valid password hash but, unlike '!', not a locked account: SSH key
# login works once a customization provides a key. The password is set at boot
# from /etc/ci4rail/user-passwords/moducop when a customization provides it.
USERADD_PARAM:${PN} = "-M -d ${MODUCOP_HOME} -s /bin/sh -p '*' moducop"

do_install() {
    install -d -m 0700 ${D}${MODUCOP_HOME}
    install -d -m 0700 ${D}${MODUCOP_HOME}/.ssh
    chown moducop:moducop ${D}${MODUCOP_HOME} ${D}${MODUCOP_HOME}/.ssh
    # The home directory is on the read-only rootfs and dropbear only reads
    # ~/.ssh/authorized_keys. Point it to the os-customization managed /etc.
    ln -s /etc/ssh/authorized_keys/moducop ${D}${MODUCOP_HOME}/.ssh/authorized_keys
    chown -h moducop:moducop ${D}${MODUCOP_HOME}/.ssh/authorized_keys
}

FILES:${PN} = "${MODUCOP_HOME}"

RDEPENDS:${PN} = "sudo"
