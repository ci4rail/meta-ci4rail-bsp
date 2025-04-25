do_before_compile() {
    sed -i -e 's/set bootargs/set bootargs fsck.mode=force fsck.repair=yes/' \
        "${WORKDIR}/boot.cmd.in"
    echo "Bootargs modified to force fsck after reboot"
}

addtask do_before_compile before do_compile after do_configure
do_before_compile[doc] = "Add bootsargs to force fsck after reboot. \
                         Enable data partition to recover from fs corruption as this partition is not ro."
