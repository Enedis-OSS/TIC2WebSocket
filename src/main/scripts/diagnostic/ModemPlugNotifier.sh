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

# Define classpath and main class
CLASSPATH="$ROOT_DIRECTORY/lib/*"
MAIN_CLASS=tic.diagnostic.modem.ModemPlugNotifierApp

# Run executable
java -cp "$CLASSPATH" $MAIN_CLASS $*