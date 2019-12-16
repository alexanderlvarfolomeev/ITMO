#!/bin/bash
while true
do
read string
if [[ "$string" = "q" ]]
then break
fi
concat_string="$concat_string$string"
done
echo "$concat_string"
