#!/bin/bash
touch data5.txt
./script5_handler.sh &
while true; do
	read LINE_G
	echo "$LINE_G" >> data5.txt
	if [[ "$LINE_G" = "QUIT" ]]
		then {
			echo "Generator will be stopped";
			sleep 1;
			exit;
		}
	fi;
done
