#!/bin/bash
echo $$ > .pid
A=0
MODE="+"
term()
{
	MODE="term"
}
trap 'term' TERM
while true; do
	case $MODE in
	"+")
		let A=$A+1
		echo $A
		;;
	"term")
		echo "Stopped by SIGTERM"
		exit
		;;
	esac
	sleep 1
done
