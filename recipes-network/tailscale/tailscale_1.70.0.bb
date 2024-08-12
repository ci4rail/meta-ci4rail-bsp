SUMMARY = "Install tailscale"
DESCRIPTION = "Tailscale client and daemon for Linux from Tailscale pre-built binaries"

HOMEPAGE = "https://github.com/tailscale/tailscale"
SECTION = "net"
LICENSE = "CLOSED"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm).*-linux"

DIRECTORY_x86-64 = "${BPN}_${PV}_amd64"
DIRECTORY_arm = "${BPN}_${PV}_arm"
DIRECTORY_aarch64 = "${BPN}_${PV}_arm64"
DIRECTORY_i586 = "${BPN}_${PV}_386"
DIRECTORY_i686 = "${BPN}_${PV}_386"

# SRC_URI_x86-64 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=amd64"
# SRC_URI_arm = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=arm"
# SRC_URI_i586 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=386"
# SRC_URI_i686 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=386"
SRC_URI_aarch64 = "https://pkgs.tailscale.com/stable/${DIRECTORY}.tgz;name=arm64"

# SRC_URI[amd64.md5sum] = "78448258bc6ff8834eaa1bac33ef114a"
# SRC_URI[amd64.sha256sum] = "b759e0185a4a2c5a927e71d3b2f6aa6c58aa293ca968397e783572a1b82da4d5"
# SRC_URI[arm.md5sum] = "d0442170446b38006433a3c9bc459053"
# SRC_URI[arm.sha256sum] = "315f105f007dde15374dda00f702215dd1b598e61b57911e01ee605f714b47bb"
SRC_URI[arm64.md5sum] = "823b66f00afe2622a26f3bb463a2799d"
SRC_URI[arm64.sha256sum] = "01302f1791118a83dbda2ec10ffe08211cc03ec7c7af153a6ba6a04b09e3a3e5"
# SRC_URI[386.md5sum] = "41dfb5b8305d7e4c2b8a2efdf465931e"
# SRC_URI[386.sha256sum] = "9400aa65dc4f7f6d2c2adcdfb1b9c991fa419e833f528964aa8a65021a5f2a2b"

SRC_URI_append = " file://tailscaled_mod.service"

inherit systemd

# Archive contents:
#   tailscale_VERSION_ARCH/systemd/tailscaled.service
#   tailscale_VERSION_ARCH/systemd/tailscaled.defaults
#   tailscale_VERSION_ARCH/tailscaled
#   tailscale_VERSION_ARCH/tailscale
#

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
    lnr ${D}${systemd_unitdir}/system/tailscaled.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/tailscaled.service
}

FILES_${PN} += "${bindir}/tailscale"
FILES_${PN} += "${sbindir}/tailscaled"
FILES_${PN} += "${sysconfdir}/default/tailscaled"
FILES_${PN} += "${systemd_unitdir}/system/tailscaled.service"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "tailscaled.service"
SYSTEMD_AUTO_ENABLE = "enable"