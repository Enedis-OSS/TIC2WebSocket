REM Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
REM
REM SPDX-FileContributor: Jehan BOUSCH
REM SPDX-FileContributor: Mathieu SABARTHES
REM
REM SPDX-License-Identifier: Apache-2.0

@echo off

REM Get script directory
set SCRIPT_DIRECTORY=%~dp0

REM Get distribution root directory (parent of script directory)
for %%I in ("%SCRIPT_DIRECTORY%..") do set ROOT_DIRECTORY=%%~fI\

REM Define log directory and config file
set LOG_DIRECTORY=%ROOT_DIRECTORY%var\log
set CONFIG_FILE=%ROOT_DIRECTORY%var\config\TIC2WebSocketConfiguration.json
set CLASSPATH=%ROOT_DIRECTORY%lib\*
set MAIN_CLASS=tic.service.TIC2WebSocketApplication

REM Run executable
java -DlogDir="%LOG_DIRECTORY%" -DconfigFile="%CONFIG_FILE%" -cp "%CLASSPATH%" %MAIN_CLASS% %*