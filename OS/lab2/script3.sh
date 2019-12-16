#!/bin/bash
touch emails.lst
grep -EohrIe "[a-zA-Z0-9_%\.\+\-]+@[a-zA-Z0-9_\-\.]+\.[a-zA-Z]+" "/etc" | 
sort -u | awk -v ORS=" " '{print}' | 
awk -v ORS=", " '{for(i = 1 ; i < NF ; i++) print $i; printf "%s", $NF}' > emails.lst
