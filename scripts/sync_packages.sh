#! /usr/bin/env bash

# needs to be rnu from build folder, in yocto context.

# upload packages for installable arch's 
bitbake package-index && rclone sync deploy/ipk/ scailx-storage:scailx-ppa/scailx/6.6-scarthgap/deploy/ipk/ --progress --transfers 64 --checkers 64 --checksum
