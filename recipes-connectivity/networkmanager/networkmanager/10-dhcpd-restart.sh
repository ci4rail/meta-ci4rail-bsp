#!/bin/bash
#
# This script restarts the DHDCP daemon whenever a network interface
# listed in /etc/default/dhcp-server comes up. 
# This is needed to provide USB attached
# io4edge devices with an IP address whenever they are restarted.
#
# Also needed for point-to-point Ethernet connections, that have no link initially.
#

interface=$1 status=$2

echo "10-dhcpd-restart running with $interface and $status"
if [[ "$2" == "up" ]]; then
  if [[ grep "$1" /etc/default/dhcp-server | grep "INTERFACES" ]]; then
    echo "Restarting dhcpd"
    systemctl restart dhcpd
  fi
fi
