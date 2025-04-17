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
                  can-utils \
                  dosfstools \
                  e2fsprogs-mke2fs \
                  exfat-utils \
                  iproute2 \
                  libgomp \
                  libgpiod-tools \
                  mtd-utils \
                  set-hostname \
                  u-boot-fw-utils \
                  udev-toradex-rules \
                  uhubctl \
                  util-linux-fstrim \
                  libusbgx \
                  libusbgx-config \
                  libusbgx-examples \
                  tdx-info \
                  udev-extraconf \
                  avahi-autoipd \
                  curl \
                  linuxptp \
                  nfs-utils-client \
                  ppp \
                  ptpd \
                  tcpdump \         
                  hostapd \
                  hostapd-example \
                  wireless-regdb-static \
                  linux-firmware-ath10k    \
                  linux-firmware-iw416     \
                  linux-firmware-nxp89xx   \
                  linux-firmware-sd8686    \
                  linux-firmware-sd8688    \
                  linux-firmware-sd8787    \
                  linux-firmware-sd8797    \
                  linux-firmware-sd8887    \
                  linux-firmware-sd8997    \
                  linux-firmware-ralink    \
                  linux-firmware-rtl8192cu \
                  linux-firmware-rtl8188   \         
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
                  kea \
                  nano \
                  coreutils \
                  tar \
                  iperf3 \
                  wireguard-tools \
                  alsa-utils \
                  wget \
                "

