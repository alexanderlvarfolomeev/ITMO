#!/bin/bash
touch errors.log
awk '/^ACPI/' `find "/var/log/" -readable -type f` > errors.log
awk '/(\/.+)+/' "errors.log"

