#!/bin/bash

src="$HOME/source";

today=$(date +"%F");
weekAgo=$(date -d "$today - 7days" +"%F");

directory=$(basename $(find "$HOME" -maxdepth 1 -type d -name 'Backup-????-??-??' | tail -n 1));
dirDate=${directory:7};

if [[ "$weekAgo" < "$dirDate" ]]; then 
	trg="$HOME/$directory";
	report="UPDATE="$(date)"=$trg\n";
	for f in $(ls "$src"); do
		if [ ! "$trg/$f" ]; then
			cp -r -t "$trg" "$src/$f";
			added="$added$f\n";
		else 
			if [[ $(stat -c%s "$trg/$f") -eq $(stat -c%s "$src/$f") ]]; then
				continue;
			else 
				cp -r -b -S ".$today" -t "$trg" "$src/$f";
				changed="$changed$f $f.$today\n";
			fi;
		fi;
	done;
	echo -e "$report$added$changed" >> "$HOME/backup-report";
else
	trg="$HOME/Backup-$today";
	mkdir "$trg";
	cp -r -t "$trg" $(find "$src" -mindepth 1 -maxdepth 1);
	echo -e "CREATION="$(date)"=$trg\n"$(ls "$src")"\n" >> "$HOME/backup-report";
fi;
