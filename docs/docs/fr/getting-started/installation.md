# Installation

## Build du projet

### Cloner le dépôt

```
git clone https://github.com/Enedis-OSS/TIC2WebSocket.git
```

### Lancer le build Maven

```
cd TIC2WebSocket
mvn clean package
```

### Vérifier la génération des artefacts

```
cd target 
ls 
``` 
Vous devriez voir les livrables générés, notamment `TIC2WebSocket-VERSION-bin.zip` ou `TIC2WebSocket-VERSION-bin.tar.gz`.

## Lancement de la documentation

### Aller dans le dossier `docs/`

```
cd docs
```

### Démarrer les services Docker de documentation

```
docker-compose up --build
``` 

### Accéder à la documentation

```
http://localhost:8000 
```
Vous devriez voir la documentation TIC2WebSocket s'afficher dans votre navigateur.
