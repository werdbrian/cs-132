#!/bin/bash

# exit on error
set -e

function install(){
  apt-get update
  apt-get install -y $@
}

function which_install(){
  if ! which $2; then
    install $1
  fi
}

sudo add-apt-repository "deb http://archive.ubuntu.com/ubuntu $(lsb_release -sc) universe"

which_install openjdk-6-jre-headless java
which_install openjdk-6-jdk javac
which_install javacc javacc
