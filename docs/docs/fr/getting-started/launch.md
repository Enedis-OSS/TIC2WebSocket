# Lancement

## Lancer le serveur TIC2WebSocket

### Linux/MacOS

Une fois le dossier compressé de TIC2WebSocket généré, extraire le contenu du dossier dans un répertoire de votre choix. 

Définir les variables d'environnement `APPLICATION_HOME` et `VERSION` pour indiquer le chemin vers le dossier d'installation et la version de TIC2WebSocket.

```bash
export APPLICATION_HOME=/chemin/vers/dossier
export VERSION=1.0.0
```

```bash
tar -xzf TIC2WebSocket-$VERSION-bin.tar.gz -C $APPLICATION_HOME --strip-components=1
cd $APPLICATION_HOME
```

Afficher l'aide de TIC2WebSocket via le lanceur avec la commande suivante :

```bash
./TIC2WebSocket.sh --help
```

Vous devriez voir les options d'aide du lanceur TIC2WebSocket s'afficher, confirmant que le serveur est prêt à être lancé.

Le serveur s'arrête avec Ctrl+C.

Pour démarrer le serveur TIC2WebSocket, utilisez la commande suivante :

```bash
./TIC2WebSocket.sh --verbose=INFO --configFile=var/config/TIC2WebSocketConfiguration.json
```

Vous devriez voir les logs du serveur TIC2WebSocket s'afficher dans la console, indiquant que le serveur est en cours d'exécution et prêt à accepter les connexions WebSocket.

```
Loading configuration file var/config/TIC2WebSocketConfiguration.json
TIC2WebSocket initialized
TIC2WebSocket starting
Starting TICCore
Starting TIC2WebSocket Netty server on localhost:19584
TIC2WebSocket Netty server started successfully on localhost:19584
TIC2WebSocket started
```

### Windows

Une fois le dossier compressé de TIC2WebSocket généré, extraire le contenu du dossier dans un répertoire de votre choix. 

Définir les variables d'environnement `APPLICATION_HOME` et `VERSION` pour indiquer le chemin vers le dossier d'installation et la version de TIC2WebSocket.

```powershell
$env:APPLICATION_HOME = "C:\chemin\vers\dossier"
$env:VERSION = "1.0.0"
```

```powershell
Unzip-File -Path TIC2WebSocket-$env:VERSION-bin.zip -DestinationPath $env:APPLICATION_HOME
cd $env:APPLICATION_HOME
```

Afficher l'aide de TIC2WebSocket via le lanceur avec la commande suivante :

```powershell
.\TIC2WebSocket.bat --help
```

Vous devriez voir les options d'aide du lanceur TIC2WebSocket s'afficher, confirmant que le serveur est prêt à être lancé.

Le serveur s'arrête avec Ctrl+C.

Pour démarrer le serveur TIC2WebSocket, utilisez la commande suivante :

```powershell
.\TIC2WebSocket.bat --verbose=INFO --configFile=var/config/TIC2WebSocketConfiguration.json
```

Vous devriez voir les logs du serveur TIC2WebSocket s'afficher dans la console, indiquant que le serveur est en cours d'exécution et prêt à accepter les connexions WebSocket.

```
Loading configuration file var/config/TIC2WebSocketConfiguration.json
TIC2WebSocket initialized
TIC2WebSocket starting
Starting TICCore
Starting TIC2WebSocket Netty server on localhost:19584
TIC2WebSocket Netty server started successfully on localhost:19584
TIC2WebSocket started
```

## Tester la connexion WebSocket

Une fois le serveur TIC2WebSocket démarré, vous pouvez tester la connexion WebSocket en utilisant le client WebSocket intégré à la documentation. Suivez les instructions du guide [Testeur WebSocket](ws-tester.md) pour vous connecter au serveur TIC2WebSocket et recevoir les données TIC en temps réel.