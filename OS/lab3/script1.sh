#!/bin/bash
ps -o "%p:%c" -u user | sed 1d > commands.lst
