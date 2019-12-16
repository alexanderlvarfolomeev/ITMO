#!/bin/bash
echo "1. Run nano"
echo "2. Run vi"
echo "3. Run links"
echo "4. Exit menu"
while true
do
read -sN 1 button
case "$button" in
	1 ) nano ;;
	2 ) vi ;;
	3 ) links ;;
	4 ) break ;;
esac
done
