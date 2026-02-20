# Utilisation

Cette section présente l'utilisation de TIC2WebSocket côté client.

## Démarche recommandée

1. Ouvrir une connexion WebSocket vers le serveur TIC2WebSocket.
2. Envoyer une requête `GetAvailableTICs` pour découvrir les flux disponibles.
3. S'abonner au(x) flux voulu(s) avec `SubscribeTIC`.
4. Traiter les évènements `OnTICData` et `OnError`.
5. Se désabonner avec `UnsubscribeTIC` avant fermeture du client.

## Référence API

Consultez la page dédiée à la référence complète des requêtes et évènements :

- [Référence API](api.md)

## Testeur WebSocket

Pour tester rapidement les requêtes/évènements avec l'interface cliente intégrée :

- [WS Tester](../getting-started/ws-tester.md)
