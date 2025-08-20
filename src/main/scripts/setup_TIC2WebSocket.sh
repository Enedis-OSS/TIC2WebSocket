#!/bin/bash

# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

############################################ VARIABLES ############################################

TIC2WebSocket_DIR=$(dirname "${BASH_SOURCE[0]}")
TIC2WebSocket_SCRIPT="${BASH_SOURCE[0]}"
TIC2WebSocket_PACKAGE="$TIC2WebSocket_DIR"/TIC2WebSocket-0.0-bin.zip
TIC2WebSocket_INSTALLDIR=/Applications/TIC2WebSocket

############################################ FUNCTIONS ############################################

function TIC2WebSocket_install()
{
	sudo mkdir -p "$TIC2WebSocket_INSTALLDIR"
	sudo unzip "$TIC2WebSocket_PACKAGE" -d "$TIC2WebSocket_INSTALLDIR"
	sudo chmod -R 777 "$TIC2WebSocket_INSTALLDIR"
}

function TIC2WebSocket_uninstall()
{
	sudo rm -Rf "$TIC2WebSocket_INSTALLDIR"
}

function TIC2WebSocket_isInstalled()
{
	"$TIC2WebSocket_INSTALLDIR"/TIC2WebSocket.sh --version > /dev/null 2>&1
	if [ $? != 0 ]; then
		echo false
		return 0
	fi
	echo true
}

############################################## MAIN ###############################################

option="$1"

case "$option" in
	install)

		if [ $(TIC2WebSocket_isInstalled) == "true" ]; then
			echo "TIC2WebSocket already installed"
		else
			echo "Install TIC2WebSocket"
			TIC2WebSocket_install
		fi
	;;

	uninstall)

		if [ $(TIC2WebSocket_isInstalled) == "true" ]; then
			echo "Uninstall TIC2WebSocket"
			TIC2WebSocket_uninstall
		else
			echo "TIC2WebSocket already uninstalled"
		fi
	;;

	reinstall)

		if [ $(TIC2WebSocket_isInstalled) == "true" ]; then
			echo "Remove TIC2WebSocket"
			TIC2WebSocket_uninstall
		fi
		echo "Install TIC2WebSocket"
		TIC2WebSocket_install
	;;

	status)

		if [ $(TIC2WebSocket_isInstalled) == "true" ]; then
			echo "TIC2WebSocket installed"
		else
			echo "TIC2WebSocket not installed"
		fi
	;;

	*)
		if [ "$option" != "" ]; then
			echo "Usage: $(basename "$TIC2WebSocket_SCRIPT") {install|uninstallreinstall|status}"
			exit 1
		fi
	;;

esac
