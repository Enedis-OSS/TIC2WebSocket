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
java -DlogDir="%SCRIPT_DIRECTORY%var\log" -DconfigFile="%SCRIPT_DIRECTORY%var\config\TIC2WebSocketConfiguration.json" -cp "%SCRIPT_DIRECTORY%/lib/*" enedis.tic.service.TIC2WebSocketApplication %*