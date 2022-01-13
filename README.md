# meta-ci4rail-bsp

Yocto BSP layer for the CI4Rail Hardware.

## Dependencies

This layer depends on:

* URI: git://git.yoctoproject.org/git/meta-yocto
  * layers: meta-poky
  * branch: dunfell

* URI: git://github.com/openembedded/openembedded-core
  * layers: meta
  * branch: dunfell

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

See [build instructions](https://github.com/ci4rail/yocto-images/tree/cleanup#building) from Ci4Rail repository for build definitons and scripts for yocto builds.

## Maintainers

* Ci4Rail GmbH `engineering@ci4rail.com`
