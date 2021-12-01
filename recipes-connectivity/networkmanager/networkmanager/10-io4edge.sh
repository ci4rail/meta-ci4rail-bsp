#!/bin/bash
#
# This script restarts the DHDCP daemon whenever a network interface
# named usb_* is restarted. This is needed to provide USB attached
# io4edge devices with an IP address whenever they are restarted
#

interface=$1 status=$2

echo "10-io4edge running with $interface and $status"
if [[ "$2" == "up" ]]; then
  if [[ "$1" == "usb_"* ]]; then
    echo "Restarting dhcp"
    systemctl restart dhcpd
  fi
fi
