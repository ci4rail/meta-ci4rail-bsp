SUMMARY = "Install Edgefarm directory structure"
LICENSE = "Apache-2.0"

# /usr/local/etc is a symlink to /etc to make it writable. Edgefarn expects wt0.ip in /usr/local/etc
# /opt/cni/bin is the default location for CNI plugins and must be present

do_install() {
    install -d ${D}/opt/cni/bin
    mkdir -p ${D}/usr/local
    ln -s /etc ${D}/usr/local/etc
}

FILES_${PN} += "/opt/cni/bin"
FILES_${PN} += "/usr/local/etc"
FILES_${PN} += "/openyurt"P