# The Toradex linux-firmware append fetches NXP's imx-firmware into WORKDIR and
# copies it into S during do_patch. create-spdx reads NO_GENERIC_LICENSE files
# from S before it gathers sources, so make the copy explicit for SPDX rebuilds
# restored from sstate or otherwise missing the patched source tree.
do_create_spdx:prepend() {
    import os
    import oe.path

    src = oe.path.join(d.getVar("WORKDIR"), "imx-firmware")
    dest = oe.path.join(d.getVar("S"), "imx-firmware")

    if os.path.isdir(src) and not os.path.exists(dest):
        oe.path.copyhardlinktree(src, dest)
}
