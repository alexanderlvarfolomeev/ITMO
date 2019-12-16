#!/bin/bash

trg="$HOME/restore";

if [ ! -d "$trg" ]; then
	mkdir "$trg";
fi;

src=$(find "$HOME" -maxdepth 1 -type d -name 'Backup-????-??-??' | tail -n 1);

cp -r -t "$trg" $(find "$src" -mindepth 1 -maxdepth 1 ! -name '*.????-??-??')
