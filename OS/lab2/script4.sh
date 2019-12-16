#!/bin/bash
awk -v path="" '/^#! *[^ \n]+/{
				path = substr($0, index($0, "/")); 
				if (index(path, " ") == 0) print path 
				else print substr(path, 0, index(path, " "))
}' `find "$HOME/lab2/scripts'n'others" \( -name "*.sh" -o -name "*.bash" \)` | 
sort | uniq -c | sort -rn | head -n1 | awk '{print $NF}'
