#!/bin/bash
touch work.txt
find "$HOME" ! -name "*links*" > work.txt
