#!/bin/bash
arr=$(cut -d: -f2,3 SleepAVG.log | tr -s ':' ' ' | 
awk '{a[$1] += substr($2, 23); b[$1]++}END{c = 1; for (i in a) {print substr(i, 18), a[i]/b[i], b[i]}}' | sort -nk1 | 
awk 'BEGIN{c = 0}{c+=$3; print $1, $2, c}')
awk -v arr="$arr" 'BEGIN{i = 0; split(arr, array, / |\n/)} {
			print; 
			if (array[3 * i + 3] == NR) {
			print "Average_Sleeping_Children_of_ParentID="array[3 * i + 1], is, array[3 * i + 2];
			i++;
		   }}' SleepAVG.log | sponge SleepAVG.log

