#!/bin/bash
if [[ "$HOME" = "$PWD" ]]
then 
echo "$HOME"
exit 0
else 
echo "ERROR: Working directory is not Home."
exit 1
fi
