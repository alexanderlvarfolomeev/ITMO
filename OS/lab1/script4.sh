#!/bin/bash
count=0
while true
do
	read number
	if [[ $[ $number%2 ] -eq 0 ]]
	then break
	fi
	(( count++ ))
done
echo "$count"
