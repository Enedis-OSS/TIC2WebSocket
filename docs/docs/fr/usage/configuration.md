# Configuration

## Emplacement

Le fichier de configuration `TIC2WebSocketConfiguration.json` se trouve dans le sous-répertoire `var/config` du répertoire d'installation de TIC2WebSocket.

## Structure

Le fichier de configuration utilise le format JSON et vous permet de modifier les paramètres suivants :

- le port d'écoute du serveur WebSocket ;
- le mode TIC (optionnel) ;
- la liste des ports série natifs (optionnel).

> ℹ️ La liste des ports USB n'est pas configurable, car TIC2WebSocket les détecte automatiquement.

Exemple de fichier de configuration :

```json
{
	"serverPort": 19584,
	"ticMode": "AUTO",
	"ticPortNames": ["/dev/ttyAMA0"]
}
```

## Paramètre `serverPort`

Le paramètre `serverPort` permet de configurer le port TCP d'écoute du serveur WebSocket TIC2WebSocket, entre `1` et `65535`.

La valeur par défaut est `19584`.

## Paramètre `ticMode`

Le paramètre `ticMode` permet de configurer le mode de flux TIC :

- `"AUTO"` : détection automatique (valeur par défaut) ;
- `"STANDARD"` : mode TIC Standard à 9600 bauds ;
- `"HISTORIC"` : mode TIC Historique à 1200 bauds.

## Paramètre `ticPortNames`

Le paramètre `ticPortNames` permet de configurer la liste des ports série natifs utilisés pour lire la TIC.

Lorsque ce paramètre est défini, la liste doit :

- contenir au moins un élément ;
- contenir des éléments uniques ;
- ne contenir aucun élément nul ;
- contenir uniquement des chaînes d'au moins un caractère.

Exemple :

```json
"ticPortNames": ["/dev/ttyAMA0"]
```
