#!/bin/bash
res=1;
op=+;
(tail -n 0 -f data5.txt) |
while true; do
	read LINE;
	case $LINE in
		\+)
			op=+;
			;;
		\*)
			op=*;
			;;
		[+-][0-9]*|[0-9]*)
			let res="$res$op$LINE";
			echo "=$res"
			;;
		QUIT)
			echo "Handler will be stopped";
			exit;
			;;
		*)
			echo "$LINE is not a number or operation";
			exit 1;
			;;
		esac
done
