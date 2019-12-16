#!/bin/bash
a=0;
echo "kill $$" | at now + 1 minutes
while true; do 
	a=$[ $a+$a ]; 
done
