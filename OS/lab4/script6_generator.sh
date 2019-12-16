#!/bin/bash
./script6_handler.sh &
while true; do
	read LINE
	case $LINE in
	TERM)
		kill -TERM $(cat .pid)
		exit;
		;;
	*)
		:
		;;
	esac
done
