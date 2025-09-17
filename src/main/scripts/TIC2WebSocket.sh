#!/bin/bash

# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

# Get script directory
SCRIPT_DIRECTORY=$(dirname $(realpath "$0"))

# Run executable
java -DlogDir="$SCRIPT_DIRECTORY/var/log" -DconfigFile="$SCRIPT_DIRECTORY/var/config/TIC2WebSocketConfiguration.json" -cp "$SCRIPT_DIRECTORY/lib/*" enedis.tic.service.TIC2WebSocketApplication $*