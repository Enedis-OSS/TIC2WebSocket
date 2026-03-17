# WebSocket Tester

Le projet inclut une interface cliente WebSocket prête à l'emploi : `ws-tester.html`.

Ce testeur permet de valider rapidement une instance TIC2WebSocket, sans développer de client spécifique.

## Rôle dans le projet

Utilisez le testeur pour :

- vérifier que le serveur WebSocket est joignable,
- découvrir les flux TIC disponibles,
- générer des requêtes JSON valides pour les principales commandes API,
- inspecter les réponses/évènements entrants en temps réel,
- reproduire des problèmes d'intégration lors du dépannage.

## Ouvrir le testeur

Ouvrez `ws-tester.html` dans votre navigateur.

Par défaut, il cible `ws://localhost:19584/`.

Vous pouvez préremplir la connexion avec des paramètres de requête :

- URL complète : `?url=ws://localhost:19584/` (alias : `wsUrl`, `ws`)
- Hôte/port : `?host=localhost&port=19584` (alias hôte : `address`, `addr`)

## Flux d'utilisation principal

1. Vérifiez l'URL cible par défaut ou ajustez l'hôte et le port si nécessaire.

	![Champs de connexion avec l'URL WebSocket, l'hôte et le port](../../assets/sceenshots_ws-tester/url_port.png)

2. Cliquez sur **Connect** pour ouvrir la session WebSocket.

	![Bouton Connect utilisé pour ouvrir la session WebSocket](../../assets/sceenshots_ws-tester/connect.png)

3. Choisissez un preset de requête dans la liste déroulante.

	![Sélecteur de preset de requête dans le WebSocket tester](../../assets/sceenshots_ws-tester/preset.png)

4. Vérifiez le payload JSON généré avant l'envoi.

	![Payload JSON généré affiché dans l'éditeur](../../assets/sceenshots_ws-tester/payload.png)

5. Cliquez sur **Send** et surveillez le panneau de logs pour suivre les échanges.

	![Panneau de logs affichant la requête envoyée et la réponse reçue](../../assets/sceenshots_ws-tester/send.png)

6. Cliquez sur **Disconnect** lorsque la session de test est terminée.

	![Bouton Disconnect utilisé pour fermer la session WebSocket](../../assets/sceenshots_ws-tester/disconnect.png)

## Presets de requêtes et identifiants

Presets disponibles :

- `GetModemsInfo`
- `GetAvailableTICs`
- `ReadTIC`
- `SubscribeTIC`
- `UnsubscribeTIC`

Les champs d'identifiant sont optionnels sauf pour `ReadTIC`, qui exige un identifiant.

Règle de priorité appliquée par le testeur :

1. `serialNumber`
2. `portId`
3. `portName`

Lorsqu'un identifiant de priorité supérieure est actif, les champs de priorité inférieure sont désactivés automatiquement.

Pour `SubscribeTIC` / `UnsubscribeTIC` :

- aucun identifiant sélectionné -> la requête s'applique à tous les flux,
- un identifiant sélectionné -> la requête cible ce flux.

Lors de la réception d'une réponse `GetAvailableTICs`, le testeur remplit automatiquement les champs d'identifiants avec la première entrée retournée.

## Logs et diagnostic

- Lignes **OUT** : JSON envoyé par le client.
- Lignes **IN** : messages reçus du serveur.
- Lignes **ERR** : erreurs de validation client ou erreurs socket.

Les messages `EVENT` sont regroupés par nom d'évènement + identifiant pour garder des logs lisibles en mode flux.

## Bonnes pratiques

- Commencez par `GetAvailableTICs` avant `ReadTIC`/`SubscribeTIC`.
- Conservez une indentation JSON à `2` pour faciliter le debug.
- Utilisez **Clear logs** avant de reproduire un incident.
- Conservez les couples requête/réponse utiles lors d'un signalement.