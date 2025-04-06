FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:verdin-imx8mp = " file://moducop-cpu01plus-standard.scc" 
SRC_URI:append:verdin-imx8mm = " file://moducop-cpu01-standard.scc" 