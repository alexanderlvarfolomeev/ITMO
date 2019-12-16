#!/bin/bash

trash="$HOME/trash"

if [ ! -d "$trash" ]; then
	mkdir "$trash";
fi

let num=$(ls -v "$trash" | tail -n 1)+1;

ln "$PWD/$1" "$trash/$num" && {
	rm "$PWD/$1";

	echo "$PWD/$1 $num" >> "$HOME/.trash.log" 
}
