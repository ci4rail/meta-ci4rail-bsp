do_before_deploy() {
    sed -i -e 's/set bootargs/set bootargs fsck.mode=force fsck.repair=yes/' \
        "${WORKDIR}/boot.cmd.in"
}

addtask do_before_deploy after do_install before do_deploy
do_before_deploy[doc] = "Add bootsargs to force fsck after reboot. \
                         Enable data partition to recover from fs corruption as this partition is not ro."
