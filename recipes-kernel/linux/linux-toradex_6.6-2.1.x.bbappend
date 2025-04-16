FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:verdin-imx8mp = " file://moducop-cpu01plus-standard.scc" 
SRC_URI:append:verdin-imx8mm = " file://moducop-cpu01-standard.scc" 

SRC_URI:append = " file://0001-moducop-specific-dts.patch \
                   file://0002-add-moducop-dts-to-Makefile.patch \
                "