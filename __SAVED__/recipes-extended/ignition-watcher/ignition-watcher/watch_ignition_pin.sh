#!/bin/sh

# IMX8MM and IXM8MP have different GPIOs for ignition pin
ignition_pin_is_off() {
  retval="false"
  if [[ $(grep Mini /proc/device-tree/model) ]]
  then
    # IMX8MM
    if [[ $(gpioget 2 4) == 0 ]]
    then
        retval="true"
    fi
  else
    # IMX8MP
    if [[ $(gpioget 0 0) == 0 ]]
    then
        retval="true"
    fi
  fi
  echo $retval
}

while true
  do
    ign_off=$(ignition_pin_is_off)
    if [ "$ign_off" == "true" ]
      then
        shutdown now
    fi
    sleep 5
done
