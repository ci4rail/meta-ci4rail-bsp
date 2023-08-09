SUMMARY = "Create symlink for kubelet root dir"
DESCRIPTION = "Create symlink for kubelet root dir to /data/kubelet"
HOMEPAGE = "https://ci4rail.com"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SYSTEMD_PACKAGES = "${PN}"

do_install() {
    install -d ${D}/${localstatedir}/lib
    ln -s /data/kubelet ${D}/${localstatedir}/lib
}

FILES_${PN} = "${localstatedir}/lib/kubelet"