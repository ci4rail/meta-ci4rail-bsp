#!/bin/sh

VENDOR="QUALCOMM"
USBDEV="1:8"

# let USB and ModemManager start
sleep 30

# check if qualcomm modem is found on USB
if ! lsusb -s ${USBDEV} | grep -qi ${VENDOR} ; then
    echo "Qualcomm modem not found. Exiting"
    exit 0
fi

# check if modem is known to ModemManager
if ! mmcli -L | grep -qi ${VENDOR}  ; then
    echo "Qualcomm modem not known to ModemManager. Restarting ModemManager."
    systemctl restart ModemManager
    echo `date`": Restarted ModemManager" >> /data/modem-restart.log
    sleep 30
    if ! mmcli -L | grep -qi ${VENDOR} ; then
        echo "Qualcomm modem still not known to ModemManager. Exiting."
        exit 1 # let systemd restart modem-restart-check.service
    fi
else
    echo "Qualcomm modem already known to ModemManager. Exiting."
    exit 0
fi