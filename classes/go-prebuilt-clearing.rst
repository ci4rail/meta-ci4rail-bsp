Go prebuilt clearing
====================

Inherit ``go-prebuilt-clearing`` in a recipe that installs Go release binaries.
See ``recipes-extended/ee-inv/ee-inv_1.0.1.bb`` for a working configuration.

Required inputs
---------------

* Add the application's release source archive to SRC_URI with a SHA-256
  checksum and a separate extraction subdirectory.
* Set GO_CLEARING_SOURCE_DIR to the extracted directory with go.mod and go.sum.
  Applications with no external binary dependencies may omit go.sum.
* Set GO_CLEARING_BINARIES to the unpacked executables, separated by spaces.
* Set GO_CLEARING_GO_VERSION to the exact release shown by ``go version -m``
  and GO_CLEARING_GO_SHA256 to the official Go source archive SHA-256.
  For a Go fork, override GO_CLEARING_GO_SOURCE_URL with a pinned tar.gz source
  archive URL and set GO_CLEARING_GO_SHA256 to that archive's checksum. The
  default URL uses go.dev/dl and GO_CLEARING_GO_VERSION. The version must still
  match each executable's build metadata. The report records the source URL.
  Tailscale provides an example using the commit in its go.toolchain.rev file.
* If a binary uses module versions absent from the source go.sum, supply a
  reviewed go.sum-format file through SRC_URI and GO_CLEARING_EXTRA_SUMS.
  Hashes must come from the checksum-pinned binary's module metadata. Conflicting
  hashes fail; the report records the supplement as unresolved source provenance.
  Tailscale 1.82.5 needs three such additional ARM64 dependency versions.

The class uses go-native only to read binary metadata; it does not execute
target binaries or compile the application. The Go version used for inspection
need not be the version embedded in the application.

Collection
----------

``bitbake -c deploy ee-inv`` fetches the collection and deploys it.
Rebuild the image to refresh its license manifest and collected legal material.
The class owns do_deploy; recipes with an existing deployment task need integration.
The class stages source collections under DEPLOY_DIR/sources/PN-PV and appends
legal files to the normal do_populate_lic output. Both deployment tasks use
sstate staging. The clearing bundle script prefers this collection over SPDX
archives of the prebuilt binary inputs.

The network-enabled do_fetch_go_clearing task runs after unpack and native
sysroot preparation. It uses BitBake's fetcher, DL_DIR, mirrors, and network
policy. ``bitbake -c fetch`` alone does not prefetch these dynamically discovered
dependencies: run ``bitbake -c fetch_go_clearing`` before an offline build.
GO_CLEARING_PROXY defaults to https://proxy.golang.org and may be overridden.
Each module zip is verified using the Go h1 hash from go.sum. All full module
entries in go.sum are retained, including test or historical dependencies;
the binary module list is recorded separately. Module archives preserve source
headers, and recognized legal files are also collected into offline text files.

Limits
------

This is evidence collection, not a completeness or legal approval check.
The JSON report records dirty builds, possible native dependencies, missing
recognized legal files, and unverified source-to-binary correspondence.
Local or versioned module replacements currently fail explicitly. Private
modules require a suitable proxy/mirror. Go workspaces, arbitrary build inputs,
embedded assets, and native libraries require additional review. The class
does not infer SPDX license expressions or replace the recipe's LICENSE.

Tests
-----

Run ``python3 -m unittest discover -s tests -p test_go_prebuilt_clearing.py``
from the layer root. Full verification additionally requires a BitBake build
and an sstate restoration check.
