#!/bin/sh

while true
  do
    ign=$(cat /dev/ignition)
    if [ "$ign" == "0" ]
      then
        shutdown now
    fi
    sleep 5
done
