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

# Define app directory
APP_DIRECTORY="$ROOT_DIRECTORY/app"

# Define classpath and main class
CLASSPATH="$APP_DIRECTORY/lib/*"
MAIN_CLASS=tic.diagnostic.modem.ModemFinderApp

# Run executable
java -cp "$CLASSPATH" $MAIN_CLASS $*
