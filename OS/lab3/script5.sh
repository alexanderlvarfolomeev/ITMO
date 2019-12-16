#!/bin/bash
awk "/^((P)?Pid)|(se\.sum_exec_runtime)|(nr_switches)/" $(find /proc -regex '/proc/[0-9]+/\(\(status\)\|\(sched\)\)' | 
sort -k4r,3n -s --field-separator="/") | 
awk -v ORS=" " '{print $NF}' | 
awk -v OFS=" : " '{ 
	count = NF/4; 
	for (i = 0; i < count; i++) {
		sleepAVG = $(4*i + 4) == 0 ? -1 : $(4*i + 3) / $(4*i + 4);
		print "ProcessID="$(4*i+1), "Parent_ProcessID="$(4*i + 2), "Average_Sleeping_Time="sleepAVG;
	}
     }' |
sort -nk3 -t= > SleepAVG.log
