#!/bin/sh

while true
  do
    if [ $(gpioget 2 4) == 0 ]
      then
        shutdown now
    fi
    sleep 5
done
