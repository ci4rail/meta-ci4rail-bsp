#!/bin/bash
while true
  do
    if [ -f /etc/scheduled-reboot.conf ]
    then
        reboot_time=$(cat /etc/scheduled-reboot.conf)
        if [[ "$reboot_time" < "$(date +%H:%M)" ]]; then
            echo "Rebooting at $reboot_time"
            /sbin/reboot
        else
            echo "Scheduled reboot time is $reboot_time"
        fi
    else
        echo "No scheduled reboot time found"
    fi
    sleep 5
done
