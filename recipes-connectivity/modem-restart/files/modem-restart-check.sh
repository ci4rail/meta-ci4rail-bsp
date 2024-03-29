#!/bin/sh

VENDOR="QUALCOMM"

# let USB and ModemManager start
sleep 30

# check if qualcomm modem is found on USB
if ! lsusb | grep -qi ${VENDOR} ; then
    echo "Qualcomm modem not found. Exiting"
    exit 0
fi

# check if modem is known to ModemManager
if ! mmcli -L | grep -qi ${VENDOR}  ; then
    echo "${VENDOR} modem not known to ModemManager. Restarting ModemManager."
    systemctl restart ModemManager
    echo `date`": Restarted ModemManager" >> /data/modem-restart.log
    sleep 30
    if ! mmcli -L | grep -qi ${VENDOR} ; then
        echo "${VENDOR} modem still not known to ModemManager. Exiting."
        exit 1 # let systemd restart modem-restart-check.service
    fi
else
    echo "${VENDOR} modem already known to ModemManager. Exiting."
    exit 0
fi