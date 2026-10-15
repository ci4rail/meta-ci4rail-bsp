# meta-ci4rail-bsp

Yocto BSP layer for the CI4Rail Hardware.

## Dependencies

This layer depends on:

* URI: git://git.yoctoproject.org/git/meta-yocto
  * layers: meta-poky
  * branch: dunfell

* URI: git://github.com/openembedded/openembedded-core
  * layers: meta
  * branch: kirkstone

* URI: git://git.openembedded.org/meta-openembedded
  * layers: meta-oe, meta-networking, meta-filesystems, meta-python, meta-xfce, meta-gnome, meta-multimedia, meta-initramfs
  * branch: dunfell

* URI: git://github.com/Freescale/meta-freescale-3rdparty
  * branch: dunfell

* URI: git://github.com/Freescale/meta-freescale-distro
  * branch: dunfell

* URI: git://github.com/Freescale/meta-freescale
  * branch: dunfell

* URI: git://git.toradex.com/meta-toradex-bsp-common
  * branch: dunfell-5.x.y

* URI: git://git.toradex.com/meta-toradex-nxp
  * branch: dunfell-5.x.y

* URI: git://github.com/meta-qt5/meta-qt5
  * branch: dunfell

* URI: git://git.toradex.com/meta-toradex-demos
  * branch: dunfell-5.x.y

* URI: git://git.toradex.com/meta-toradex-distro
  * branch: dunfell-5.x.y

## Build

See [build instructions](https://github.com/ci4rail/yocto-images#building) from Ci4Rail repository for build definitons and scripts for yocto builds.

## Maintainers

* Ci4Rail GmbH `engineering@ci4rail.com`

## Mender application rollback

`recipes-mender/mender-docker-compose` installs the `app` v3 Update Module and
its Docker Compose v2 helper (Compose v1 and Podman are unsupported). Installation saves a persistent transaction under
`/data/mender-app/.transactions`, including resolved manifests and image archives.
Rollback stops a partially installed composition, restores the previous images
and tags, and restarts the previous services using an image-ID override. A first
installation rolls back to no application. Backups survive `ArtifactCommit` and
are released by `Cleanup` after successful commit or rollback. Failed recovery
keeps its backup and locks that application against further updates.

Readiness checking is optional and defaults to disabled. Configure
`/etc/mender/mender-app.conf`:

```sh
APP_HEALTHCHECK_ENABLED=yes
APP_HEALTHCHECK_TIMEOUT=60
APP_HEALTHCHECK_INTERVAL=2
```

When enabled, services with container health checks must become healthy;
services without checks must be running. Intentional one-shot services require
the Compose label `io.ci4rail.mender.oneshot: "true"` and must exit with code 0.
Checks run after rollout and before commit. Settings are retained for the whole
transaction, including rollback. Choose a timeout that includes startup and the
configured container health-check intervals.

The previous composition must have a running or stopped container for every
service so the module can identify its exact image. Missing containers, inactive
profiles, and mixed image versions among replicas require operator attention
before an update. The module fails before altering the application in these
cases. Use named volumes or external bind mounts for persistent application data;
volume contents, writable container layers and database migrations are outside
application-image rollback. Ensure sufficient `/data` space for old image
archives, new payloads and delta working files. Shared runtime images are not
automatically deleted or pruned.

Whole-archive binary deltas require identical base archive bytes on the artifact
builder and device. Modern Docker exports can include current timestamps in tar
headers, so repeated `docker image save` calls may differ even for the same image.
This pre-existing format constraint is separate from rollback; use full image
payloads or layer deltas when exports are not reproducible. Ordinary deltas are
reconstructed before loading any new images to avoid moving a shared base tag.

The module writes `.mender-compose.yml` and `.mender-images.yml` in the deployed
manifest directory to retain resolved configuration and rollback image choices.
Those filenames are reserved for the module. Artifact metadata may provide
uppercase environment variable names for Compose substitution and runtime
commands; module paths and health-check configuration remain device-controlled.
For an existing installation from the older module, its manifests and current
environment must still resolve correctly for the first rollback snapshot.

If recovery fails, fix the reported engine/storage problem and retry
`ArtifactRollback` with the original module file tree, then `Cleanup`. If the
client has removed that tree, create a temporary tree containing
`tmp/app-transaction` with the retained transaction directory's absolute path.
Use that tree and the same module configuration to retry recovery. Do not remove
the transaction lock to permit a new deployment before recovery succeeds.

Target verification lives in the image repository's
`tests/yocto_tests/test_application_module.py`. It exercises the installed
modules with isolated projects and image payloads, including injected process
termination. These tests do not replace physical power-loss testing or a full
Mender server deployment test.
