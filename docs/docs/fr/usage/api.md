# Référence API

La communication avec TIC2WebSocket repose sur des messages JSON échangés via WebSocket.

## GetAvailableTICs

Cette requête permet de récupérer la liste des flux TIC disponibles.

### Requête

```json
{
  "type": "REQUEST",
  "name": "GetAvailableTICs"
}
```

### Réponse

La réponse contient une liste de structures avec les informations TIC :

- numéro de série ;
- nom du port série ;
- identifiant du port USB physique.

```json
{
  "type": "RESPONSE",
  "name": "GetAvailableTICs",
  "dateTime": "10/06/2021 14:01:00",
  "errorCode": 0,
  "data": [
    {
      "serialNumber": "021762010203",
      "portName": "/dev/ttyUSB0",
      "portId": "1-1"
    },
    {
      "serialNumber": "041775010507",
      "portName": "/dev/ttyUSB2",
      "portId": "1-2"
    }
  ]
}
```

## GetModemsInfo

Cette requête permet de récupérer la liste des informations des modems TIC connectés.

La reconnaissance des modems se fait à partir de la paire USB `vendorId` + `productId`.

Types de modems pris en charge :

| Type de modem | Vendor ID (hex) | Product ID (hex) | Vendor ID (dec) | Product ID (dec) |
| --- | --- | --- | --- | --- |
| `MICHAUD` | `0x0403` | `0x6001` | `1027` | `24577` |
| `TELEINFO` | `0x0403` | `0x6015` | `1027` | `24597` |

### Requête

```json
{
  "type": "REQUEST",
  "name": "GetModemsInfo"
}
```

### Réponse

La réponse contient une liste de structures avec les informations des modems :

- nom du port série ;
- type de modem ;
- identifiant produit (USB PID), si disponible ;
- identifiant vendeur (USB VID), si disponible ;
- nom du produit, si disponible ;
- nom du fabricant, si disponible ;
- numéro de série du modem, si disponible ;
- identifiant physique du port série, si disponible.

```json
{
  "type": "RESPONSE",
  "name": "GetModemsInfo",
  "dateTime": "16/06/2021 15:53:32",
  "errorCode": 0,
  "data": [
    {
      "portName": "COM3",
      "modemType": "MICHAUD",
      "productId": 24597,
      "vendorId": 1027,
      "productName": null,
      "manufacturerName": "FTDI",
      "serialNumber": "DA2VYTKGA",
      "portId": null
    }
  ]
}
```

## ReadTIC

Cette requête permet de lire une trame TIC.

### Requête

```json
{
  "type": "REQUEST",
  "name": "ReadTIC",
  "data": {
    "serialNumber": "010203040506"
  }
}
```

Le champ `data` doit contenir un identifiant TIC défini dans le [format d'identifiant TIC](#format-didentifiant-tic).

### Réponse

```json
{
  "type": "RESPONSE",
  "name": "ReadTIC",
  "dateTime": "10/06/2021 14:01:00",
  "errorCode": 0,
  "data": {
    "mode": "STANDARD",
    "captureDateTime": "10/06/2021 14:01:00",
    "ticIdentifier": {
      "serialNumber": "021762010203",
      "portName": "/dev/ttyUSB0",
      "portId": "1-1"
    },
    "content": {
      "ADSC": "010203040506",
      "URMS1": 230
    }
  }
}
```

## SubscribeTIC

Cette requête permet de s'abonner à un flux TIC. Une fois abonné, le client reçoit des évènements à chaque nouvelle trame TIC ou erreur liée au flux TIC.

### Requête

Plusieurs formats sont possibles.

Abonnement à un flux TIC :

```json
{
  "type": "REQUEST",
  "name": "SubscribeTIC",
  "data": {
    "serialNumber": "010203040506"
  }
}
```

Abonnement à plusieurs flux TIC :

```json
{
  "type": "REQUEST",
  "name": "SubscribeTIC",
  "data": [
    {
      "serialNumber": "010203040506"
    },
    {
      "portId": "1-1"
    }
  ]
}
```

Abonnement à tous les flux TIC :

```json
{
  "type": "REQUEST",
  "name": "SubscribeTIC"
}
```

Le champ `data` doit contenir un identifiant TIC (ou une liste d'identifiants TIC) défini dans le [format d'identifiant TIC](#format-didentifiant-tic).

### Réponse

Réponse en cas de succès :

```json
{
  "type": "RESPONSE",
  "name": "SubscribeTIC",
  "dateTime": "10/06/2021 14:01:00",
  "errorCode": 0
}
```

Si le flux TIC demandé n'existe pas :

```json
{
  "type": "RESPONSE",
  "name": "SubscribeTIC",
  "dateTime": "10/06/2021 14:01:00",
  "errorCode": -7,
  "errorMessage": "The given TIC identifier doesn't match with current TIC stream"
}
```

## Évènement `OnTICData`

Cet évènement est généré à chaque nouvelle trame TIC et envoyé à tous les clients abonnés à cette TIC.

```json
{
  "type": "EVENT",
  "dateTime": "10/06/2021 14:01:00",
  "name": "OnTICData",
  "data": {
    "mode": "STANDARD",
    "captureDateTime": "10/06/2021 14:01:00",
    "ticIdentifier": {
      "serialNumber": "021762010203",
      "portName": "/dev/ttyUSB0",
      "portId": "1-1"
    },
    "content": {
      "ADSC": "010203040506",
      "URMS1": 230
    }
  }
}
```

## Évènement `OnError`

Cet évènement est généré lorsqu'une erreur survient sur le flux TIC et envoyé à tous les clients abonnés à cette TIC.

```json
{
  "type": "EVENT",
  "dateTime": "10/06/2021 14:01:00",
  "name": "OnError",
  "data": {
    "errorCode": -5,
    "errorMessage": "TIC frame reading timeout",
    "ticIdentifier": {
      "serialNumber": "021762010203",
      "portName": "/dev/ttyUSB0",
      "portId": "1-1"
    }
  }
}
```

## UnsubscribeTIC

Cette requête permet de se désabonner d'un flux TIC.

### Requête

Plusieurs formats sont possibles.

Désabonnement d'un flux TIC :

```json
{
  "type": "REQUEST",
  "name": "UnsubscribeTIC",
  "data": {
    "serialNumber": "010203040506"
  }
}
```

Désabonnement de plusieurs flux TIC :

```json
{
  "type": "REQUEST",
  "name": "UnsubscribeTIC",
  "data": [
    {
      "serialNumber": "010203040506"
    },
    {
      "portId": "1-1"
    }
  ]
}
```

Désabonnement de tous les flux TIC :

```json
{
  "type": "REQUEST",
  "name": "UnsubscribeTIC"
}
```

Dans le cas d'un désabonnement ciblé, le champ `data` doit contenir un identifiant TIC (ou une liste d'identifiants TIC) défini dans le [format d'identifiant TIC](#format-didentifiant-tic).

## Format d'identifiant TIC

Un identifiant TIC est un objet contenant un des champs suivants :

- `serialNumber`
- `portId`
- `portName`

Selon la requête, vous pouvez fournir un identifiant unique ou une liste d'identifiants.

### Réponse

```json
{
  "type": "RESPONSE",
  "name": "UnsubscribeTIC",
  "dateTime": "16/06/2021 15:53:32",
  "errorCode": 0
}
```