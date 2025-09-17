REM Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
REM
REM SPDX-FileContributor: Jehan BOUSCH
REM SPDX-FileContributor: Mathieu SABARTHES
REM
REM SPDX-License-Identifier: Apache-2.0

@echo off

REM Get script directory
set SCRIPT_DIRECTORY=%~dp0

REM Run executable
java -cp "%SCRIPT_DIRECTORY%/lib/*" enedis.lab.io.tic.TICPortPlugNotifier %*