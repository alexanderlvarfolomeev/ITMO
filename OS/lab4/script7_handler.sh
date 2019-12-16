#!/bin/bash
echo $$ > .pid
A=1
MODE="+"
usr1()
{
	MODE="+"
}
usr2()
{
	MODE="*"
}
term()
{
	MODE="term"
}
trap 'usr1' USR1
trap 'usr2' USR2
trap 'term' TERM
while true; do
	case "$MODE" in
	\+)
		let A="$A"+2;
		echo "$A"
		;;
	\*)
		let A="$A"*2;
		echo "$A"
		;;
	term)
		echo "Stopped by SIGTERM"
		exit
		;;
	esac
	sleep 1
done
