SUMMARY = "Install tailscale"
DESCRIPTION = "Tailscale client and daemon for Linux from Tailscale pre-built binaries"

HOMEPAGE = "https://github.com/tailscale/tailscale"
SECTION = "net"
# Licenses listed by the v1.82.5 CLI/daemon dependency inventory. The inventory
# covers multiple platforms, so this conservatively includes all listed licenses.
LICENSE = "BSD-3-Clause & Apache-2.0 & BSD-2-Clause & ISC & MIT"
# Vendored from https://github.com/tailscale/tailscale/tree/v1.82.5:
# LICENSE and licenses/tailscale.md. Refresh both when updating the binaries.
LIC_FILES_CHKSUM = "\
    file://${WORKDIR}/LICENSE.tailscale;md5=a672713a9eb730050e491c92edf7984d \
    file://${WORKDIR}/tailscale-dependencies.md;md5=94f3c508a2deade85d6fe7f33618f735 \
"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

inherit go-prebuilt-clearing

SRC_URI[clearing_source.sha256sum] = "09685251057c96b7aa01e62b982a90824559ffd7113c8b69b9fc454c611f40a9"
GO_CLEARING_SOURCE_DIR = "${WORKDIR}/clearing-source/tailscale-${PV}"
GO_CLEARING_BINARIES = "${WORKDIR}/${DIRECTORY}/tailscale ${WORKDIR}/${DIRECTORY}/tailscaled"
GO_CLEARING_GO_VERSION = "go1.24.2"
# Pinned by go.toolchain.rev in the v1.82.5 source release.
GO_CLEARING_GO_SOURCE_URL = "https://github.com/tailscale/go/archive/982da8f24fa0504f2214f24b0d68b2febd5983f8.tar.gz"
GO_CLEARING_GO_SHA256 = "def08ac193acee4477893b382b4fc01184297f5f874f4897e3140881f35e61ce"
# Official ARM64 binaries include three dependency versions absent from the tag.
GO_CLEARING_EXTRA_SUMS = "${WORKDIR}/tailscale-binary.go.sum"

DIRECTORY:x86-64 = "${BPN}_${PV}_amd64"
DIRECTORY:arm = "${BPN}_${PV}_arm"
DIRECTORY:aarch64 = "${BPN}_${PV}_arm64"
DIRECTORY:i586 = "${BPN}_${PV}_386"
DIRECTORY:i686 = "${BPN}_${PV}_386"

# SRC_URI_x86-64 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=amd64"
# SRC_URI_arm = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=arm"
# SRC_URI_i586 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=386"
# SRC_URI_i686 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=386"
SRC_URI:aarch64 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=arm64"

# SRC_URI[amd64.md5sum] = "78448258bc6ff8834eaa1bac33ef114a"
# SRC_URI[amd64.sha256sum] = "b759e0185a4a2c5a927e71d3b2f6aa6c58aa293ca968397e783572a1b82da4d5"
# SRC_URI[arm.md5sum] = "d0442170446b38006433a3c9bc459053"
# SRC_URI[arm.sha256sum] = "315f105f007dde15374dda00f702215dd1b598e61b57911e01ee605f714b47bb"
SRC_URI[arm64.md5sum] = "f7e6a974f2c45d70e9d9894c1a99e448"
SRC_URI[arm64.sha256sum] = "8361aa3bc0cbbfd79363fef4e16b86cb4b8b5b18d186f9bc5c2dfcdf981d9d44"
# SRC_URI[386.md5sum] = "41dfb5b8305d7e4c2b8a2efdf465931e"
# SRC_URI[386.sha256sum] = "9400aa65dc4f7f6d2c2adcdfb1b9c991fa419e833f528964aa8a65021a5f2a2b"

SRC_URI:append = " \
    https://github.com/tailscale/tailscale/archive/refs/tags/v${PV}.tar.gz;name=clearing_source;downloadfilename=${BPN}-${PV}-source.tar.gz;subdir=clearing-source \
    file://tailscaled_mod.service \
    file://LICENSE.tailscale \
    file://tailscale-dependencies.md \
    file://tailscale-binary.go.sum \
"

inherit systemd

do_install() {
    install -d ${D}/${bindir}
    install -d ${D}/${sbindir}
    install -d ${D}${sysconfdir}/default/
    install -d ${D}${systemd_unitdir}/system/
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/

    install ${WORKDIR}/${DIRECTORY}/tailscale ${D}/${bindir}/tailscale
    install ${WORKDIR}/${DIRECTORY}/tailscaled ${D}/${sbindir}/tailscaled
    install -m 0644 ${WORKDIR}/${DIRECTORY}/systemd/tailscaled.defaults ${D}${sysconfdir}/default/tailscaled
    install -m 0644 ${WORKDIR}/tailscaled_mod.service ${D}${systemd_unitdir}/system/tailscaled.service
    ln -rs ${D}${systemd_unitdir}/system/tailscaled.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/tailscaled.service
}

FILES:${PN} += "${bindir}/tailscale"
FILES:${PN} += "${sbindir}/tailscaled"
FILES:${PN} += "${sysconfdir}/default/tailscaled"
FILES:${PN} += "${systemd_unitdir}/system/tailscaled.service"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "tailscaled.service"
SYSTEMD_AUTO_ENABLE = "enable"
