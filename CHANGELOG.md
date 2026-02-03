<!--
  ~ Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
  ~ 
  ~ SPDX-FileContributor: Mathieu SABARTHES
  ~ 
  ~ SPDX-License-Identifier: Apache-2.0
-->
# Changelog

[🇫🇷 Français](CHANGELOG.fr.md) | [🇺🇸 English](CHANGELOG.md)

## [v2.0.0](https://github.com/Enedis-OSS/TIC2WebSocket/tree/v2.0.0)
### ✨ New features:
- Added macOS serial port discovery (`SerialPortFinderForMacOsX`)
- New diagnostic CLI utilities (core, modem, serial port, USB)
- Added a test interface via an HTML page

### 🔧 Improvements & fixes:
- Refactored project architecture and simplified code structure
- Fixed logger warnings

## [v1.0.0](https://github.com/Enedis-OSS/TIC2WebSocket/tree/v1.0.0)
### ✨ New features:
- Serial port connection and reading of TIC (Télé-Information Client) frames
- WebSocket server to broadcast TIC data in real-time
- Support for different TIC modes (historical and standard)
- Flexible configuration via properties file
- Detailed logging for diagnosis and debugging
- Robust error handling and automatic reconnection
- Simple WebSocket API for integration with other applications