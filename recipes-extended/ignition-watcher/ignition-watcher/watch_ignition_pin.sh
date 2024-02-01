#!/bin/sh

# test if /etc/ignition_shutdown_inhibit exists
if [ -f /etc/ignition_shutdown_inhibit ]
  then
    echo "/etc/ignition_shutdown_inhibit exists, watching ignition pin is inhibited"
    exit 0
fi

while true
  do
    ign=$(cat /dev/ignition)
    if [ "$ign" == "0" ]
      then
        shutdown now
    fi
    sleep 5
done
