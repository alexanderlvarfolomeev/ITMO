#!/bin/bash

trash="$HOME/trash"
logfile="$HOME/.trash.log";
readarray -t lines < <(awk -v name="$1" '/^(\/.+)*\/'"$1"' /{print substr($0, 0, length($0) - length(name) - length($NF) - 1), $NF}' "$HOME/.trash.log");

for line in "${lines[@]}"; do	
	targetDir=$(echo "$line" | awk '{print $1}');
	num=$(echo "$line" | awk '{print $2}');
	echo "Recover file $targetDir/$1? (Y/N)";
	read -N 1 conf;
	case "$conf" in
	Y ) {
		if [ ! -d "$targetDir" ]; then
			echo "File will be recovered in $HOME, because $targetDir no longer exists";
			targetDir="$HOME";
		fi;
		ln "$trash/$num" "$targetDir/$1" && {
			rm "$trash/$num";
			sed -i '/ '"$num"'$/d' "$logfile";
		}
		break;
	} ;;
	N ) echo; continue ;;
	* ) echo; continue ;;
esac
done
