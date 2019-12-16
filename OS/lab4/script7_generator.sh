#!/bin/bash
./script7_handler.sh &
while true; do
	read LINE
	case $LINE in
	\+)
		kill -USR1 $(cat .pid)
		;;
	\*)
		kill -USR2 $(cat .pid)
		;;
	TERM)
		kill -TERM $(cat .pid)
		sleep 1
		exit
		;;
	*)
		:
		;;
	esac
done
