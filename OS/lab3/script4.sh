#!/bin/bash
for PID in `ps -eo pid | sed 1d`
do
	awk -v pid="$PID" -v OFS=":" '{print pid, $2 - $3 }' "/proc/$PID/statm"
done | sort -nrk"2" -t":"
