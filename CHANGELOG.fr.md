<!--
  ~ Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
  ~ 
  ~ SPDX-FileContributor: Mathieu SABARTHES
  ~ 
  ~ SPDX-License-Identifier: Apache-2.0
-->
# Journal des modifications

[🇫🇷 Français](CHANGELOG.fr.md) | [🇺🇸 English](CHANGELOG.md)

## [v2.0.0](https://github.com/Enedis-OSS/TIC2WebSocket/tree/v2.0.0)
### ✨ Nouvelles fonctionnalités:
- Ajout de la découverte des ports série sous macOS (`SerialPortFinderForMacOsX`)
- Nouveaux utilitaires CLI de diagnostic (core, modem, port série, USB)
- Ajout d'une interface de test via une page HTML

### 🔧 Améliorations & corrections:
- Refactorisation de l'architecture du projet et simplification de la structure de code
- Correction des avertissements du logger

## [v1.0.0](https://github.com/Enedis-OSS/TIC2WebSocket/tree/v1.0.0)
### ✨ Nouvelles fonctionnalités:
- Connexion et lecture des trames TIC (Télé-Information Client) via port série
- Serveur WebSocket pour diffuser les données TIC en temps réel
- Support des différents modes TIC (historique et standard)
- Configuration flexible via fichier de propriétés
- Logging détaillé pour le diagnostic et le débogage
- Gestion robuste des erreurs et reconnexion automatique
- API WebSocket simple pour l'intégration avec d'autres applications