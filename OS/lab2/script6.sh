#!/bin/bash
wc -l `find "/var/log" -type f -readable -name "*.log"` | tail -n1 | awk '{print $1}'
