#!/bin/bash

IFACE="$1"
DNSMASQ_BIN="/usr/bin/dnsmasq"
IP_BIN="/usr/sbin/ip"

# Define IP and DHCP range per interface
case "$IFACE" in
  usb_io_ctrl)
    IP="192.168.200.10"
    RANGE="192.168.200.1,192.168.200.1"
    ;;
  usb_ext1)
    IP="192.168.201.10"
    RANGE="192.168.201.1,192.168.201.1"
    ;;
  usb_ext2)
    IP="192.168.202.10"
    RANGE="192.168.202.1,192.168.202.1"
    ;;
  usb_ext3)
    IP="192.168.203.10"
    RANGE="192.168.203.1,192.168.203.1"
    ;;
  usb_ext4)
    IP="192.168.204.10"
    RANGE="192.168.204.1,192.168.204.1"
    ;;
  usb_ext5)
    IP="192.168.205.10"
    RANGE="192.168.205.1,192.168.205.1"
    ;;
  usb_ext6)
    IP="192.168.206.10"
    RANGE="192.168.206.1,192.168.206.1"
    ;;
  usb_ext7)
    IP="192.168.207.10"
    RANGE="192.168.207.1,192.168.207.1"
    ;;
  usb_ext8)
    IP="192.168.208.10"
    RANGE="192.168.208.1,192.168.208.1"
    ;;
  *)
    echo "Unknown interface: $IFACE" >&2
    exit 1
    ;;
esac

PIDFILE="/run/dnsmasq-$IFACE.pid"

echo "Setting up interface $IFACE with static IP $IP and DHCP range $RANGE" 
# Bring up interface with static IP
${IP_BIN} addr flush dev "$IFACE"
${IP_BIN} addr add "$IP/24" dev "$IFACE"
${IP_BIN} link set "$IFACE" up

systemctl stop dnsmasq-$IFACE || true

# Start dnsmasq. Run it as a systemd service to detach it from udev.
systemd-run --unit=dnsmasq-$IFACE \
  --collect \
  --service-type=exec \
  $DNSMASQ_BIN \
  --keep-in-foreground \
  --interface="$IFACE" \
  --bind-interfaces \
  --conf-file= \
  --no-hosts \
  --domain="$IFACE.local" \
  --dhcp-range="$RANGE",12h \
  --pid-file="$PIDFILE" \
  --dhcp-leasefile=/var/lib/misc/dnsmasq-$IFACE.leases \
  --port=5353

