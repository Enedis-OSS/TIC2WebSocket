<!--
  ~ Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
  ~ 
  ~ SPDX-FileContributor: Jehan BOUSCH
  ~ SPDX-FileContributor: Mathieu SABARTHES
  ~ 
  ~ SPDX-License-Identifier: Apache-2.0
-->

# TIC2WebSocket
*Tele Information Client (TIC) WebSocket Interface*

[![REUSE status](https://api.reuse.software/badge/git.fsfe.org/reuse/api)](https://api.reuse.software/info/git.fsfe.org/reuse/api)

[🇫🇷 Français](README.fr.md) | [🇺🇸 English](README.md)

## Summary

* [Introduction](#introduction)
* [Installation](#installation)
* [Usage Example](#usage_example)
* [Documentation](#documentation)
* [Contributing](#contrib)
* [Support](#support)
* [Contributors](#contributors)

## <a name="introduction"></a> Introduction

The **TIC2WebSocket** application is used as a generic interface for accessing the Tele Information Client (TIC).

TIC2WebSocket provides a WebSocket-based API for managing and interfacing with TIC data, enabling real-time communication and data exchange for client telemetry information.

## <a name="installation"></a> Installation

### Prerequisites

To generate/install the application, you need the following prerequisites:

- [Java](https://www.java.com/download/)
- [Maven](https://maven.apache.org/download.cgi)

### Installation

To install the **TIC2WebSocket** application, follow these steps:

#### Generate target files
When you are at the project root, simply type the following command:

```bash 
mvn clean package
```

As a result, the *target* directory is created, containing the output .zip file.

#### Extract .zip file
Unzip the .zip file into the folder of your choice.

🐧 **Linux**

Extract zip
```bash 
unzip target/TIC2WebSocket-VERSION-bin -d /path/to/your/folder
```

💻 **Windows**

Extract `target/TIC2WebSocket-VERSION-bin` in the folder of your choice.

### Starting the Application

To start the **TIC2WebSocket** application, execute the launch script:

🐧 **Linux**

Extract zip
```bash 
cd /path/to/your/folder
./TIC2WebSocket.sh
```

💻 **Windows**

Run `TIC2WebSocket.bat`

### Help

Add `--help` option to get basic information on how to use the launcher.
```bash 
./TIC2WebSocket.sh --help
```

## <a name="documentation"></a> Documentation

[TODO]

## <a name="contrib"></a> Contributing ?

![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)

See the [Getting started](GETTING_STARTED.md), which document how to install and setup the required environment for developing

You don't need to be a developer to contribute, nor do much, you can simply:
* Enhance documentation,
* Correct a spelling,
* [Report a bug](https://github.com/Enedis-OSS/TIC2WebSocket/issues/new/choose)
* [Ask a feature](https://github.com/Enedis-OSS/TIC2WebSocket/issues/new/choose)
* [Give us advices or ideas](https://github.com/Enedis-OSS/TIC2WebSocket/issues/new/choose),
* etc.

To help you start, we invite you to read [Contributing](CONTRIBUTING.md), which gives you rules and code conventions to respect

To contribute to this documentation (README, CONTRIBUTING, etc.), we conform to the [CommonMark Spec](https://spec.commonmark.org/)

## <a name="contributors"></a> Contributors

Core contributors :
* **Jehan BOUSCH** (<jehan-externe.bousch@enedis.fr>)
* **Mathieu SABARTHES** (<matthieu-externe.sabarthes@enedis.fr>)

We strive to provide a benevolent environment and support any [contribution](#contrib).
