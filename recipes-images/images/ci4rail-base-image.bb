SUMMARY ?= "Ci4Rail Embedded Linux Base Image"
DESCRIPTION ?= "Minimal image for the Ci4Rail Hardware"

LICENSE ?= "MIT"

inherit core-image

#Prefix to the resulting deployable tarball name
IMAGE_BASENAME ?= "Base-Image"
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
                  packagegroup-benchmark-tdx-cli \
                  packagegroup-devel-tdx-cli \
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
                  libusbgx-examples\
                  sqlite3 \
                  rng-tools \
                  util-linux \
                  networkmanager \
                  modemmanager \
                  dhcp-server \
                  chrony \
                  chronyc \
                  io4edge-cli \
                  ttynvt \
                  python3-pyserial \
                  overlay-directories \
                  persistent-journald \
                  nano \
                "
