
# patch graphroot in storage.conf to point to writeable directory
do_install:append() {
    # Drop in a systemd override to create the directory at runtime
    install -d ${D}${sysconfdir}/systemd/system/podman.service.d
    cat << 'EOF' > ${D}${sysconfdir}/systemd/system/podman.service.d/graphroot-dir.conf
[Service]
ExecStartPre=/bin/mkdir -p /data/containers/storage
ExecStartPre=/bin/chown root:root /data/containers/storage
ExecStartPre=/bin/chmod 0755 /data/containers/storage
EOF

    sed -i 's|^graphroot = .*|graphroot = "/data/containers/storage"|' \
        ${D}${sysconfdir}/containers/storage.conf
}