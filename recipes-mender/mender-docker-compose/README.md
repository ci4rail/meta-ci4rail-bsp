# Docker Compose application updates

This recipe installs the Mender `app` Update Module and its `docker-compose`
helper. Use Mender artifacts with payload type **`app`** and metadata orchestrator
**`docker-compose`**. The target requires Docker and Docker Compose v2.
Application updates do not require a reboot.

Each application has a stable `application_name`, which is also its Compose
project name. Use lowercase letters, digits, underscores and hyphens, starting
with a letter or digit. Keep this name across releases; give each artifact a new
release name. Different applications must have different project names.

## Compose manifests

Package a `manifests/` directory containing `compose.yaml` (or
`docker-compose.yml`) and any files it references. Use prebuilt ARM64 images for
CPU01 and CPU01Plus. Include each required image in the artifact for installation
without registry access. Set `pull_policy: never` to use the packaged images.

For example, this service runs without a health endpoint or Docker health check:

```yaml
services:
  worker:
    image: busybox:1.36.1
    pull_policy: never
    restart: unless-stopped
    command: ["sh", "-c", "while :; do echo working; sleep 30; done"]
```

The readiness check requires this container to be running, neither restarting
nor paused. This verifies container state, not application-level readiness.

For a service that exposes an HTTP endpoint, define a Docker health check. The
probe command must be available inside the image. For example, using BusyBox:

```yaml
services:
  web:
    image: busybox:1.36.1
    pull_policy: never
    restart: unless-stopped
    command:
      - sh
      - -c
      - mkdir -p /www; echo ready > /www/index.html; exec httpd -f -p 8080 -h /www
    ports:
      - "8080:8080"
    healthcheck:
      test: ["CMD-SHELL", "wget -q -O /dev/null http://127.0.0.1:8080/"]
      interval: 2s
      timeout: 1s
      retries: 10
      start_period: 5s
```

For an intentional one-shot service, add the following label and disable
restarting. The module waits for it to exit with status zero:

```yaml
services:
  initialize:
    image: busybox:1.36.1
    pull_policy: never
    restart: "no"
    command: ["sh", "-c", "echo initialization complete"]
    labels:
      io.ci4rail.mender.oneshot: "true"
```

Store application data in named volumes or absolute bind mounts under `/data`.
Relative bind mounts resolve under the deployed manifests directory. Container
and image rollback does not undo changes to volumes, bind-mounted data or
external systems; design initialization and schema changes accordingly.

Do not include `.mender-compose.yml` or `.mender-images.yml` in your manifests:
these filenames are reserved for the module's resolved configuration.

## Build a full-image artifact

On a development/build machine, install Docker, `mender-artifact`, and standard
shell tools. The following example packages the worker composition above. Start
in a fresh working directory and save it as `manifests/compose.yaml`.

```sh
set -eu
mkdir -p images/worker
image_ref=busybox:1.36.1

docker pull --platform linux/arm64 "$image_ref"
docker image save -o images/worker/image.img "$image_ref"

# Identical current/new references select a full image rather than a delta.
printf '%s\n' "$image_ref" > images/worker/url-new.txt
cp images/worker/url-new.txt images/worker/url-current.txt
sha256sum images/worker/image.img | cut -d ' ' -f 1 > images/worker/sums-new.txt
cp images/worker/sums-new.txt images/worker/sums-current.txt

tar -czf images.tar.gz images
tar -czf manifests.tar.gz manifests
cat > meta-data.json <<'JSON'
{
  "version": "1.0",
  "application_name": "example-app",
  "orchestrator": "docker-compose",
  "platform": "linux/arm64"
}
JSON
```

For multiple images, create one subdirectory per image under `images/`, each
with the files shown above. Image references must match the Compose manifest.
The metadata platform describes the payload; pull/build for that architecture
before saving images.

Read the target's Mender device type:

```sh
# Run on the target.
cat /var/lib/mender/device_type
```

Use the value after `device_type=` when building the artifact on the build
machine:

```sh
device_type='REPLACE_WITH_TARGET_DEVICE_TYPE'
mender-artifact write module-image \
  -T app \
  -t "$device_type" \
  -n example-app-v1 \
  -m meta-data.json \
  -f images.tar.gz \
  -f manifests.tar.gz \
  -o example-app-v1.mender
```

This creates an unsigned development artifact. Production artifacts must go
through the project's CI signing process and satisfy the target's artifact
verification policy.

Optional metadata `env` values provide Compose interpolation variables as
strings, for example:

```json
"env": {"LOG_LEVEL": "info"}
```

Reference these in the manifest, for example with
`environment: {LOG_LEVEL: "${LOG_LEVEL}"}`. Module settings such as
`APP_HEALTHCHECK_*` and `PERSISTENT_STORE` cannot be overridden through artifact
metadata. The resolved configuration is saved so rollback uses the previous
release's values.

## Deploy and commit

Upload the artifact to the Mender server and deploy it to compatible devices.
The Mender client drives installation, commit and rollback.

For a standalone development update, copy the artifact to the target and run
as root, with no server deployment running concurrently:

```sh
mender-update install /data/example-app-v1.mender
# Inspect application behavior before accepting the update.
docker compose --project-name example-app \
  --project-directory /data/mender-app/example-app/manifests ps
mender-update commit
```

After a successful install, use `mender-update rollback` instead of `commit` to
reject the pending release. Keep the same `application_name` and build a new
artifact name, such as `example-app-v2`, for the next update. Rollback snapshots
are removed during Mender Cleanup after a completed transaction; they are not a
permanent release history.

The module checks readiness after rollout and again at commit. A failure causes
the Mender update to fail and enter its rollback flow. When a previous release
exists, rollback restores its saved images and Compose configuration without
requiring registry access. On a first installation, rollback removes the new
composition. Updates stop the old composition before starting the new one, so
allow for service downtime.

## Health-check configuration

The installed `/etc/mender/mender-app.conf` defaults to:

```sh
APP_HEALTHCHECK_ENABLED=yes
APP_HEALTHCHECK_TIMEOUT=60
APP_HEALTHCHECK_INTERVAL=2
```

Timeout and polling interval are positive integer seconds. The timeout applies
to each readiness check across the whole composition. Increase it for services
with longer initialization or health-check start periods. Containers with a
Docker health check must report `healthy`; containers without one need only
pass the running-state check. Labeled one-shot services must exit successfully.

Set `APP_HEALTHCHECK_ENABLED=no` explicitly to skip readiness checks. Effective
settings are retained for the transaction, including subsequent commit and
rollback calls. Existing devices whose configuration explicitly says `no` must
be updated to `yes` to enable checks; the new default does not override that
choice.

## Storage, rollback and troubleshooting

Application files live in `/data/mender-app/<application_name>`. Persistent
transaction snapshots live under `/data/mender-app/.transactions` until Cleanup.
Allow space for incoming payloads, extracted images, previous image archives and
any delta working files. `PERSISTENT_STORE` in `mender-app.conf` can select a
different persistent location.

A first installation rejects an existing Compose project with the same name if
it has no module-managed manifests. For subsequent updates, each previous
service must still have a container, running or stopped, so the module can
snapshot its exact image. Avoid manually deleting managed containers between
updates. The module does not globally prune Docker images.

Useful target diagnostics:

```sh
journalctl -u mender-updated.service
cat /data/mender-app/example-app/manifests/compose.log
docker ps -a --filter label=com.docker.compose.project=example-app
docker inspect CONTAINER_ID
```

For readiness failures, inspect `State` and `State.Health` in `docker inspect`,
then check container logs with `docker logs CONTAINER_ID`. If rollback fails,
transaction backups and the per-application lock are retained for recovery.
Preserve them while diagnosing the failure.

Manifest-only updates use the same artifact structure with an empty `images/`
directory in `images.tar.gz`; all referenced images must already be available
on the target when using `pull_policy: never`.

The module also supports whole-image and layer deltas. Whole-image deltas require
an exact, reproducible base `docker image save` archive; exports can differ even
for the same image. Use the full-image workflow above unless your packaging
pipeline provides compatible delta generation and base-image handling.
