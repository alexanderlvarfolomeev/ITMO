#!/bin/bash
mkdir "$HOME/test" && {
	echo "catalog test was created successfully" >> "$HOME/report";
	filename=$(date -d $(ps -o start "$$" | tail -n 1) +"%d_%b_%Y_%T");
	touch "$HOME/test/$filename"
} ; {
	tmpfile=$(mktemp /tmp/tmp.XXXXXX);
	ping -c 1 "www.net-nikogo.ru" > "$tmpfile" 2>&1 || echo $(cat < "$tmpfile") >> "$HOME/report";
	rm "$tmpfile"
}

