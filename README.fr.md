<!--
  ~ Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
  ~ 
  ~ SPDX-FileContributor: Jehan BOUSCH
  ~ SPDX-FileContributor: Mathieu SABARTHES
  ~ 
  ~ SPDX-License-Identifier: Apache-2.0
-->

# TIC2WebSocket
*Interface WebSocket pour les données TIC (Télé Information Client)*

[![REUSE status](https://api.reuse.software/badge/git.fsfe.org/reuse/api)](https://api.reuse.software/info/git.fsfe.org/reuse/api)

[🇫🇷 Français](README.fr.md) | [🇺🇸 English](README.md)

## Sommaire

* [Introduction](#introduction)
* [Installation](#installation)
* [Exemple d'utilisation](#usage_example)
* [Documentation](#documentation)
* [Contribuer](#contrib)
* [Support](#support)
* [Contributeurs](#contributors)

## <a name="introduction"></a> Introduction

L'application **TIC2WebSocket** est utilisée comme interface générique pour accéder aux données TIC (Télé Information Client).

TIC2WebSocket fournit une API basée sur WebSocket pour gérer les données TIC, permettant une communication en temps réel et un échange de données pour les informations de télémétrie client.

## <a name="installation"></a> Installation

### Prérequis

Pour générer/installer l'application, vous avez besoin des prérequis suivants :

- [Java](https://www.java.com/download/)
- [Maven](https://maven.apache.org/download.cgi)

### Installation

Pour installer l'application **TIC2WebSocket**, suivez ces étapes :

#### Générer les fichiers cible
Lorsque vous êtes à la racine du projet, tapez simplement la commande suivante :

```bash 
mvn clean package
```

En conséquence, le répertoire *target* est créé, contenant le fichier .zip de sortie.

#### Extraire le fichier .zip
Décompressez le fichier .zip dans le dossier de votre choix.

🐧 **Linux**

Extraire le zip
```bash 
unzip target/TIC2WebSocket-*-bin -d /chemin/vers/votre/dossier
```

💻 **Windows**

Extrayez `target/TIC2WebSocket-*-bin` dans le dossier de votre choix.

### Démarrage de l'Application

Pour démarrer l'application **TIC2WebSocket**, exécutez le script de lancement :

🐧 **Linux**

Extraire le zip
```bash 
cd /chemin/vers/votre/dossier
./TIC2WebSocket.sh
```

💻 **Windows**

Exécutez `TIC2WebSocket.bat`

### Aide

Ajoutez l'option `--help` pour obtenir des informations de base sur l'utilisation du lanceur.
```bash 
./TIC2WebSocket.sh --help
```

## <a name="documentation"></a> Documentation

[TODO]

## <a name="contrib"></a> Contribuer ?

![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)

Consultez le guide [Getting started](GETTING_STARTED.md), qui documente comment installer et configurer l'environnement requis pour le développement

Vous n'avez pas besoin d'être développeur pour contribuer, ni de faire beaucoup, vous pouvez simplement :
* Améliorer la documentation,
* Corriger une faute d'orthographe,
* [Signaler un bug](https://github.com/Enedis-OSS/TIC2WebSocket/issues/new/choose)
* [Demander une fonctionnalité](https://github.com/Enedis-OSS/TIC2WebSocket/issues/new/choose)
* [Nous donner des conseils ou des idées](https://github.com/Enedis-OSS/TIC2WebSocket/issues/new/choose),
* etc.

Pour vous aider à démarrer, nous vous invitons à lire [Contributing](CONTRIBUTING.md), qui vous donne les règles et conventions de code à respecter

Pour contribuer à cette documentation (README, CONTRIBUTING, etc.), nous nous conformons à la [CommonMark Spec](https://spec.commonmark.org/)

## <a name="contributors"></a> Contributeurs

Contributeurs principaux :
* **Jehan BOUSCH** (<jehan-externe.bousch@enedis.fr>)
* **Mathieu SABARTHES** (<matthieu-externe.sabarthes@enedis.fr>)

Nous nous efforçons de fournir un environnement bienveillant et de soutenir toute [contribution](#contrib).
