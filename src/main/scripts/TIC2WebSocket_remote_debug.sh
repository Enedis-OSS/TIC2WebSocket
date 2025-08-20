#!/bin/bash

# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

# Get script directory
SCRIPT_DIRECTORY=$(dirname "$0")

# Run executable
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=0.0.0.0:8000 -DlogDir="$SCRIPT_DIRECTORY/var/log" -DconfigDir="$SCRIPT_DIRECTORY/var/config" -cp "$SCRIPT_DIRECTORY/lib/*" tic.service.app.TIC2WebSocketApplication $*