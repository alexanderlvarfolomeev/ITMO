#!/bin/bash
cut -d: -f '1 3' "/etc/passwd" | tr ":" " " | sort -n -k2
