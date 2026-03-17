# Configuration

## Location

The `TIC2WebSocketConfiguration.json` configuration file is located in the `var/config` subdirectory of the TIC2WebSocket installation directory.

## Structure

The configuration file uses JSON and allows you to change:

- the listening port of the WebSocket server,
- the TIC mode (optional),
- the list of native serial ports (optional).

> ℹ️ The USB port list is not configurable, because TIC2WebSocket detects them automatically.

Example configuration file:

```json
{
	"serverPort": 19584,
	"ticMode": "AUTO",
	"ticPortNames": ["/dev/ttyAMA0"]
}
```

## `serverPort` parameter

`serverPort` configures the TCP listening port of the TIC2WebSocket WebSocket server, between `1` and `65535`.

The default value is `19584`.

## `ticMode` parameter

`ticMode` configures the TIC stream mode:

- `"AUTO"`: automatic detection (default),
- `"STANDARD"`: Standard TIC mode at 9600 baud,
- `"HISTORIC"`: Historic TIC mode at 1200 baud.

## `ticPortNames` parameter

`ticPortNames` configures the list of native serial ports used to read TIC.

When set, the list must:

- contain at least one element,
- contain unique elements,
- contain no null element,
- contain only strings with at least one character.

Example:

```json
"ticPortNames": ["/dev/ttyAMA0"]
```
