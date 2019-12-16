#!/bin/bash
nice -n 20 ./script.sh &
nice -n 10 ./script.sh &
top

