SUMMARY ?= "Ci4Rail Embedded Linux Base Image"
DESCRIPTION ?= "Minimal image for the Ci4Rail Hardware"

LICENSE ?= "MIT"

inherit core-image

#Prefix to the resulting deployable tarball name
BASENAME ?= "Base-Image"
export IMAGE_BASENAME = "${BASENAME}"
MACHINE_NAME ?= "${MACHINE}"
GIT_VERSION := "${@d.getVar('BB_ORIGENV',False).getVar('IMAGE_GIT_VERSION', False) or 'NoVersion'}"
NAME_SUFFIX := "${@d.getVar('BB_ORIGENV',False).getVar('IMAGE_NAME_SUFFIX', False) or ''}"
IMAGE_NAME = "${MACHINE_NAME}_${IMAGE_BASENAME}_${GIT_VERSION}${NAME_SUFFIX}"

# Copy Licenses to image /usr/share/common-license
COPY_LIC_MANIFEST ?= "1"
COPY_LIC_DIRS ?= "1"

require classes/ci4rail_add_rootfs_version.inc

IMAGE_LINGUAS = "en-us"

IMAGE_FEATURES += "\
                   read-only-rootfs \
                   package-management \
                   "

ROOTFS_RO_UNNEEDED ?= "update-rc.d base-passwd"

IMAGE_INSTALL += "\
                  packagegroup-boot \
                  packagegroup-basic \
                  packagegroup-base-tdx-cli \
                  packagegroup-machine-tdx-cli \
                  packagegroup-networking-tdx-cli \
                  packagegroup-wifi-tdx-cli \
                  packagegroup-wifi-fw-tdx-cli \
                  udev-extraconf \
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
                  procps \
                  libusbgx \
                  rng-tools \
                  util-linux \
                  networkmanager \
                  modemmanager \
                  dhcp-server \
                  chrony \
                  chronyc \
                  io4edge-cli \
                  ttynvt \
                  ttynvt-runner \
                  socketcan-io4edge \
                  socketcan-io4edge-runner \
                  overlay-directories \
                  persistent-journald \
                  nano \
                  coreutils \
                  tar \
                  iperf3 \
                  wireguard-module \
                  wireguard-tools \
                "


