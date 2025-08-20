#!/bin/bash

# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

if [ ! -n "$1" ]
then
  echo "Usage: `basename $0` portName [ticMode]"
  exit -1
fi
portName="$1"

if [ ! -n "$2" ]; then
  ticMode="STANDARD"
else
  ticMode="$2"
fi

if [ "$ticMode" == "STANDARD" ]; then
  baudrate=9600
elif [ "$ticMode" == "HISTORIC" ]; then
  baudrate=1200
else
  echo "The tic mode \"$ticMode\" is unknown (only \"STANDARD\" or \"HISTORIC\" is allowed)!"
  exit -1 
fi

# Configure serial port
stty $baudrate evenp igncr -F "$portName"
# Read serial port
cat "$portName"
