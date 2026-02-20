# Usage

This section explains how to use TIC2WebSocket from the client side.

## Recommended approach

1. Open a WebSocket connection to the TIC2WebSocket server.
2. Send a `GetAvailableTICs` request to discover available streams.
3. Subscribe to the desired stream(s) using `SubscribeTIC`.
4. Handle `OnTICData` and `OnError` events.
5. Unsubscribe using `UnsubscribeTIC` before closing the client.

## API reference

See the dedicated page for the full list of requests and events:

- [API Reference](api.md)

## WebSocket tester

To quickly test requests/events using the built-in client UI:

- [WS Tester](../getting-started/ws-tester.md)
