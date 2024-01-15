#!/usr/bin/env bash
# install
echo "我们需要存储权限，请选择 ‘y’"
termux-setup-storage
pkg update
pkg install openjdk-17 tree -y

# run
java -jar DeviceInformationGet.jar
tput reset
