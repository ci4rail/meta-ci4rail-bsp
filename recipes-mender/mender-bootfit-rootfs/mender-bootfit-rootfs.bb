SUMMARY = "Mender paired boot FIT and dm-verity rootfs Update Module"
DESCRIPTION = "Installs a Mender Update Module that writes a signed boot FIT and matching dm-verity rootfs into the inactive A/B slot before activating it."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://bootfit-rootfs"

inherit allarch

# bootFIT/rootfs-A/rootfs-B/data is p1/p2/p3/p4. The boot filesystem
# contains one FIT file for each rootfs slot.
CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_PART ?= "${TORADEX_MENDER_BOOTFIT_PART}"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_ROOTFS_PART_A ?= "${MENDER_ROOTFS_PART_A}"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_ROOTFS_PART_B ?= "${MENDER_ROOTFS_PART_B}"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_SLOT_A_VALUE ?= "${MENDER_ROOTFS_PART_A_NUMBER}"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_SLOT_B_VALUE ?= "${MENDER_ROOTFS_PART_B_NUMBER}"

CI4RAIL_MENDER_BOOTFIT_ROOTFS_SLOT_ENV ?= "mender_boot_part"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_UPGRADE_ENV ?= "upgrade_available"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTCOUNT_ENV ?= "bootcount"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_PAYLOAD_PATTERN ?= "fitImage*"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_TARGET_FILE_A ?= "/fitImage-A"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_TARGET_FILE_B ?= "/fitImage-B"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_ROOTFS_PAYLOAD_PATTERN ?= "*.ext4.verity"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_REQUIRE_DM_VERITY_ROOT ?= "1"
CI4RAIL_MENDER_BOOTFIT_ROOTFS_CHECK_CMDLINE_ROOT ?= "1"

do_install() {
    install -d ${D}${datadir}/mender/modules/v3
    install -m 0755 ${WORKDIR}/bootfit-rootfs \
        ${D}${datadir}/mender/modules/v3/bootfit-rootfs

    install -d ${D}${sysconfdir}/mender
    cat > ${D}${sysconfdir}/mender/bootfit-rootfs.conf <<EOF
# Slot block devices for bootFIT/rootfs-A/rootfs-B/data.
BOOTFIT_PART="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_PART}"
ROOTFS_PART_A="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_ROOTFS_PART_A}"
ROOTFS_PART_B="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_ROOTFS_PART_B}"

# U-Boot environment selector values identifying the corresponding rootfs/FIT
# pair. The boot command and rollback path must interpret this same variable.
BOOT_SLOT_ENV="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_SLOT_ENV}"
SLOT_A_VALUE="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_SLOT_A_VALUE}"
SLOT_B_VALUE="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_SLOT_B_VALUE}"
UPGRADE_ENV="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_UPGRADE_ENV}"
BOOTCOUNT_ENV="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTCOUNT_ENV}"

# A module-image Artifact must contain exactly one file matching each pattern.
BOOTFIT_PAYLOAD_PATTERN="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_PAYLOAD_PATTERN}"
BOOTFIT_TARGET_FILE_A="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_TARGET_FILE_A}"
BOOTFIT_TARGET_FILE_B="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_BOOTFIT_TARGET_FILE_B}"
ROOTFS_PAYLOAD_PATTERN="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_ROOTFS_PAYLOAD_PATTERN}"

# A successful new-slot boot must mount root through dm-verity.
REQUIRE_DM_VERITY_ROOT="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_REQUIRE_DM_VERITY_ROOT}"
CHECK_CMDLINE_ROOT="${CI4RAIL_MENDER_BOOTFIT_ROOTFS_CHECK_CMDLINE_ROOT}"
EOF
}

FILES:${PN} += " \
    ${datadir}/mender/modules/v3/bootfit-rootfs \
    ${sysconfdir}/mender/bootfit-rootfs.conf \
"

RDEPENDS:${PN} += " \
    mender-flash \
    u-boot-fw-utils \
    util-linux-findfs \
    util-linux-findmnt \
    util-linux-mount \
    util-linux-umount \
"
