#!/bin/bash
ps -eo cmd,pid | awk '/^\/sbin\/.+/{ print $NF }' > sbin.log
