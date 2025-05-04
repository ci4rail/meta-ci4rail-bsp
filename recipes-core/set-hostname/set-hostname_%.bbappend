# install our version of sethostname script
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RDEPENDS:${PN}:append = " jq ee-inv"