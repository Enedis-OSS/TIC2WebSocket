# API Reference

Communication with TIC2WebSocket is based on JSON messages exchanged over WebSocket.

## GetAvailableTICs

This request returns the list of available TIC streams.

### Request

```json
{
  "type": "REQUEST",
  "name": "GetAvailableTICs"
}
```

### Response

The response contains a list of TIC information objects:

- serial number;
- serial port name;
- physical USB port identifier.

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

This request returns modem information for connected TIC modems.

Modem recognition is based on the USB pair `vendorId` + `productId`.

Supported modem types:

| Modem type | Vendor ID (hex) | Product ID (hex) | Vendor ID (dec) | Product ID (dec) |
| --- | --- | --- | --- | --- |
| `MICHAUD` | `0x0403` | `0x6001` | `1027` | `24577` |
| `TELEINFO` | `0x0403` | `0x6015` | `1027` | `24597` |

### Request

```json
{
  "type": "REQUEST",
  "name": "GetModemsInfo"
}
```

### Response

The response contains a list of modem information objects:

- serial port name;
- modem type;
- product identifier (USB PID), if available;
- vendor identifier (USB VID), if available;
- product name, if available;
- manufacturer name, if available;
- modem serial number, if available;
- physical serial port identifier, if available.

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

This request reads one TIC frame.

### Request

```json
{
  "type": "REQUEST",
  "name": "ReadTIC",
  "data": {
    "serialNumber": "010203040506"
  }
}
```

The `data` field must contain a TIC identifier defined in [TIC identifier format](#tic-identifier-format).

### Response

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

This request subscribes to one or more TIC streams. Once subscribed, the client receives events for each new TIC frame or stream-related error.

### Request

Several request formats are available.

Subscribe to one TIC stream:

```json
{
  "type": "REQUEST",
  "name": "SubscribeTIC",
  "data": {
    "serialNumber": "010203040506"
  }
}
```

Subscribe to multiple TIC streams:

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

Subscribe to all TIC streams:

```json
{
  "type": "REQUEST",
  "name": "SubscribeTIC"
}
```

The `data` field must contain one TIC identifier (or a list of TIC identifiers) defined in [TIC identifier format](#tic-identifier-format).

### Response

Success response:

```json
{
  "type": "RESPONSE",
  "name": "SubscribeTIC",
  "dateTime": "10/06/2021 14:01:00",
  "errorCode": 0
}
```

If the requested TIC stream does not exist:

```json
{
  "type": "RESPONSE",
  "name": "SubscribeTIC",
  "dateTime": "10/06/2021 14:01:00",
  "errorCode": -7,
  "errorMessage": "The given TIC identifier doesn't match with current TIC stream"
}
```

## `OnTICData` event

This event is generated for each new TIC frame and sent to all clients subscribed to that TIC.

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

## `OnError` event

This event is generated when an error occurs on the TIC stream and sent to all clients subscribed to that TIC.

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

This request unsubscribes from one or more TIC streams.

### Request

Several request formats are available.

Unsubscribe from one TIC stream:

```json
{
  "type": "REQUEST",
  "name": "UnsubscribeTIC",
  "data": {
    "serialNumber": "010203040506"
  }
}
```

Unsubscribe from multiple TIC streams:

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

Unsubscribe from all TIC streams:

```json
{
  "type": "REQUEST",
  "name": "UnsubscribeTIC"
}
```

For targeted unsubscription, the `data` field must contain one TIC identifier (or a list of TIC identifiers) defined in [TIC identifier format](#tic-identifier-format).

## TIC identifier format

A TIC identifier is an object containing one of the following fields:

- `serialNumber`
- `portId`
- `portName`

Depending on the request, you can provide a single identifier or a list of identifiers.

### Response

```json
{
  "type": "RESPONSE",
  "name": "UnsubscribeTIC",
  "dateTime": "16/06/2021 15:53:32",
  "errorCode": 0
}
```
