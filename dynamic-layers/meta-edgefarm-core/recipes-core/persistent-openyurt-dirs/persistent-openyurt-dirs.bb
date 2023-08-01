SUMMARY = "Create persistent directories for k8s and openyurt"
DESCRIPTION = "Create persistent directories for k8s and openyurt"
HOMEPAGE = "https://ci4rail.com"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI = "file://persistent-openyurt-dirs.service"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit systemd
inherit features_check

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "persistent-openyurt-dirs.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/persistent-openyurt-dirs.service ${D}${systemd_unitdir}/system/

    install -d ${D}/${localstatedir}/lib

    ln -s /data/kubelet ${D}/${localstatedir}/lib/kubelet
    ln -s /data/openyurt ${D}/${localstatedir}/lib/openyurt
    ln -s /data/yurthub ${D}/${localstatedir}/lib/yurthub
    ln -s /data/opt_cni_run ${D}/opt/cni/run
}

REQUIRED_DISTRO_FEATURES= "systemd"
