inherit core-image

SUMMARY = "Ci4Rail Embedded Linux Image for HW bringup"
DESCRIPTION = "Ci4Rail Embedded Linux Image for HW bringup. Copied from tdx-minimal-reference-image"

LICENSE = "MIT"

#Prefix to the resulting deployable tarball name
export IMAGE_BASENAME = "Bringup-Image"
MACHINE_NAME ?= "${MACHINE}"
GIT_VERSION := "${@d.getVar('BB_ORIGENV',False).getVar('IMAGE_GIT_VERSION', False) or 'NoVersion'}"
NAME_SUFFIX := "${@d.getVar('BB_ORIGENV',False).getVar('IMAGE_NAME_SUFFIX', False) or ''}"
IMAGE_VERSION =  "CI.OS.LMP-${GIT_VERSION}"
IMAGE_NAME = "${MACHINE_NAME}_${IMAGE_BASENAME}_${IMAGE_VERSION}${NAME_SUFFIX}"


# Copy Licenses to image /usr/share/common-license
COPY_LIC_MANIFEST ?= "1"
COPY_LIC_DIRS ?= "1"

add_rootfs_version () {
    printf "${DISTRO_NAME} ${DISTRO_VERSION} (${DISTRO_CODENAME}) \\\n \\\l\n" > ${IMAGE_ROOTFS}/etc/issue
    printf "${DISTRO_NAME} ${DISTRO_VERSION} (${DISTRO_CODENAME}) %%h\n" > ${IMAGE_ROOTFS}/etc/issue.net
    printf "${IMAGE_NAME}\n\n" >> ${IMAGE_ROOTFS}/etc/issue
    printf "${IMAGE_NAME}\n\n" >> ${IMAGE_ROOTFS}/etc/issue.net
}
# add the rootfs version to the welcome banner
ROOTFS_POSTPROCESS_COMMAND += " add_rootfs_version;"

IMAGE_LINGUAS = "en-us"
#IMAGE_LINGUAS = "de-de fr-fr en-gb en-us pt-br es-es kn-in ml-in ta-in"

CONMANPKGS ?= "connman connman-plugin-loopback connman-plugin-ethernet connman-plugin-wifi connman-client"

# Entropy source daemon
RANDOM_HELPER = "rng-tools"
RANDOM_HELPER_tegra124 = "haveged"

USB_GADGET = ""
USB_GADGET_imx = " \
    libusbgx \
    libusbgx-examples \
"

IMAGE_INSTALL += " \
    packagegroup-boot \
    packagegroup-basic \
    packagegroup-base-tdx-cli \
    packagegroup-benchmark-tdx-cli \
    packagegroup-devel-tdx-cli \
    packagegroup-machine-tdx-cli \
    packagegroup-networking-tdx-cli \
    packagegroup-wifi-tdx-cli \
    packagegroup-wifi-fw-tdx-cli \
    udev-extraconf \
    ${CONMANPKGS} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'timestamp-service systemd-analyze', '', d)} \
    bzip2 \
    e2fsprogs \
    e2fsprogs-resize2fs \
    e2fsprogs-tune2fs \
    ethtool \
    gpsd \
    grep \
    lsof \
    minicom \
    mmc-utils-cos \
    pciutils \
    phytool \
    procps \
    ${RANDOM_HELPER} \
    stress-ng \
    sqlite3 \
    tdx-oak-sensors \
    ${USB_GADGET} \
    util-linux \
    cpuburn-a53 \
    clpeak \
    memtester \
    pcimem \
    dhrystone \
    stressapptest \
    tinymembench \
    whetstone \
    fio \
    lshw \
    hwdata \
    pciutils \
    minicom \
    nano \
"