#!/bin/bash

# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

# Get script directory
SCRIPT_DIRECTORY=$(dirname $(realpath "$0"))

# Get distribution root directory (parent of script directory)
ROOT_DIRECTORY=$(realpath "$SCRIPT_DIRECTORY/..")

# Define log directory and config file
LOG_DIRECTORY="$ROOT_DIRECTORY/var/log"
CONFIG_FILE="$ROOT_DIRECTORY/var/config/TIC2WebSocketConfiguration.json"
CLASSPATH="$ROOT_DIRECTORY/lib/*"
MAIN_CLASS=tic.service.TIC2WebSocketApplication

# Run executable
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=0.0.0.0:8000 -DlogDir="$LOG_DIRECTORY" -DconfigFile="$CONFIG_FILE" -cp "$CLASSPATH" $MAIN_CLASS $*