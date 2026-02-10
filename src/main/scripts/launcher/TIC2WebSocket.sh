#!/bin/bash

# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

# Get script directory
SCRIPT_DIRECTORY=$(dirname $(realpath "$0"))

# App directory is the script directory
APP_DIRECTORY="$SCRIPT_DIRECTORY"

# Define log directory and config file
LOG_DIRECTORY="$APP_DIRECTORY/var/log"
CONFIG_FILE="$APP_DIRECTORY/var/config/TIC2WebSocketConfiguration.json"
CLASSPATH="$APP_DIRECTORY/lib/*"
MAIN_CLASS=tic.service.TIC2WebSocketApplication

# Run executable
java -DlogDir="$LOG_DIRECTORY" -DconfigFile="$CONFIG_FILE" -cp "$CLASSPATH" $MAIN_CLASS $*