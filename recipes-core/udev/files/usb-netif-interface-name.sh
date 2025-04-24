#!/bin/sh

USB_PATH=$(readlink /sys/class/net/$1)

USB_PORT=$(echo $USB_PATH | awk -F/ '{print$(NF-3)}')

case $USB_PORT in

  1-1.4.1)
    echo "usb_ext1"
    ;;

  1-1.4.2)
    echo "usb_ext2"
    ;;

  1-1.4.4)
    echo "usb_ext3"
    ;;

  1-1.4.3)
    echo "usb_ext4"
    ;;

  1-1.3.1)
    echo "usb_ext5"
    ;;

  1-1.3.3)
    echo "usb_ext6"
    ;;

  1-1.3.2)
    echo "usb_ext7"
    ;;

  1-1.3.4)
    echo "usb_ext8"
    ;;

  1-1.2.1)
    echo "usb_io_ctrl"
    ;;

  *)
    echo "unknown"
    ;;

esac
