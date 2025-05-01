#!/bin/bash

#
# If /etc/scheduled-reboot.conf exists, it should contain a time in HH:MM format
#
while true
  do
    if [ -f /etc/scheduled-reboot.conf ]
    then
        reboot_time=$(cat /etc/scheduled-reboot.conf)
        echo "Reboot time is $reboot_time"
        if [[ "$reboot_time" == "$(date +%H:%M)" ]]; then
            echo "Rebooting at $reboot_time"
            /sbin/reboot
        fi
    else
        echo "No /etc/scheduled-reboot.conf found"
    fi
    sleep 15
done