CPU01 standard image logging
===========================

This recipe mounts `/data/log` at `/var/log`, using the writable Mender data
partition so logs survive reboot and rootfs A/B updates. `VOLATILE_LOG_DIR`
is disabled in the common kas configuration to provide a real mount point.
The prepare service creates the backing directory after `/data` is mounted;
the journal flush and tmpfiles services run after the bind mount.

Journald uses persistent storage with `SystemMaxUse=100M`. It rotates journal
files at 10M or one day and automatically removes old archived journals to
respect its disk budget. Active files and available disk space affect actual
usage; this is journald's retention limit, not a partition quota. Early boot
logs use a bounded 16M runtime journal and are flushed to disk on startup.
Journald handles rotation itself; external logrotate must not rotate journals.
Docker already uses the journald driver, so its logs share this budget.

After flashing, verify on the target:

    findmnt /var/log
    systemctl status var-log.mount systemd-journal-flush.service
    journalctl --disk-usage
    journalctl --list-boots

The mount should use `/data/log`. After a reboot, `--list-boots` should include
the previous boot. 
