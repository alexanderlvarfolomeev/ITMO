#!/bin/bash
touch full.log
for message in "Warning:" "Information:"; do
	marker="\(${message:0:1}${message:0:1}\)"
	awk -v msg="$message" "/\[ *[0-9]+\.[0-9]+\] $marker/{sub(/$marker/, msg); print}" "/home/user/.local/share/xorg/Xorg.0.log"
done > full.log

