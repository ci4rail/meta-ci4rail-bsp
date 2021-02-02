# meta-ci.os
CI4Rail Top Level Yocto Layer

## Ci4Rail recipes:
* `recipes-overlays/overlay-directorie: Overlay mounts for `/etc/docker`, `/etc/iotedge` and `/var/lib/iotedge`. The owner of `/var/lib/iotedge` is changed to `iotedge:iotedge`.

## 3rd Party layer modifications:
* `recipes-images/images/tdx-reference-minimal-image.bbappend`: Modifies variables to enable versioning using environment variables: `IMAGE_GIT_VERSION` for versioning and `IMAGE_NAME_SUFFIX` for tagging a development build.

* `recipes-core/base-files/base_files_3.0.14.bbappend`: fixes `fstab` file to work with read-only filesystem. Read-only filesystem is needed for mender delta-update support, but `meta-toradex-demo` provides an own fstab that is not compatible with `openembedded-core`'s way to enable read-only. Enabling read-only is done with sed by patching the fstab file (https://github.com/openembedded/openembedded-core/blob/master/meta/classes/rootfs-postcommands.bbclass#L95-L98). This won't work with the fstab provided by `meta-toradex-demo`.

* `recipes-containers/docker`: adds a `/etc/docker/daemon.json` with a modified root-dir and a specific dns server.
