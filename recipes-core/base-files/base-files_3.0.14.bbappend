# Fix `fstab` file to work with read-only filesystem.
# For read-only filesystem `meta-toradex-demo` provides an own fstab that is not compatible with `openembedded-core`'s way to enable read-only.
# Enabling read-only is done with sed by patching the [fstab file](https://github.com/openembedded/openembedded-core/blob/707036d4ec12ef1a260adcef78627b26e32e6540/meta/classes/rootfs-postcommands.bbclass#L93-L96).
# This won't work with the fstab provided by `meta-toradex-demo`.

FILESEXTRAPATHS_prepend := "${THISDIR}/base-files:"

do_install_append () {
    # create mount point for sdcard
    install -d ${D}/media/sdcard
    # create symlink so that boot loader can find the dtb
    ln -s ${D}/boot/imx8mm-verdin-wifi-moducop-cpu01.stb ${D}/boot/imx8mm-verdin-wifi-dev.dtb
    ln -s ${D}/boot/imx8mp-verdin-wifi-moducop-cpu01.stb ${D}/boot/imx8mp-verdin-wifi-dev.dtb
}