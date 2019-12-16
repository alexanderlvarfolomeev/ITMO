#!/bin/bash
man bash | tr -cs 'A-Za-z_' '[\n*]' | awk '{if (length($0) >= 4) print}' | sort | uniq -c | sort -n -k1 | tail -n3 | awk '{print $2}'
