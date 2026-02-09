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

REM Define app directory
set APP_DIRECTORY=%ROOT_DIRECTORY%app\

REM Define log directory and config file
set LOG_DIRECTORY=%APP_DIRECTORY%var\log
set CONFIG_FILE=%APP_DIRECTORY%var\config\TIC2WebSocketConfiguration.json
set CLASSPATH=%APP_DIRECTORY%lib\*
set MAIN_CLASS=tic.service.TIC2WebSocketApplication

REM Run executable
java -DlogDir="%LOG_DIRECTORY%" -DconfigFile="%CONFIG_FILE%" -cp "%CLASSPATH%" %MAIN_CLASS% %*