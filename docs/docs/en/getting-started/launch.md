# Launch

## Start the TIC2WebSocket server

### Linux/macOS

Once the TIC2WebSocket archive has been generated, extract it into a directory of your choice.

Set the `APPLICATION_HOME` and `VERSION` environment variables to indicate the installation folder path and the TIC2WebSocket version.

```bash
export APPLICATION_HOME=/path/to/folder
export VERSION=1.0.0
```

```bash
tar -xzf TIC2WebSocket-$VERSION-bin.tar.gz -C $APPLICATION_HOME --strip-components=1
cd $APPLICATION_HOME
```

Show the TIC2WebSocket launcher help with:

```bash
./TIC2WebSocket.sh --help
```

You should see the help options, confirming the server is ready to be started.

Stop the server with Ctrl+C.

To start the TIC2WebSocket server, use:

```bash
./TIC2WebSocket.sh --verbose=INFO --configFile=var/config/TIC2WebSocketConfiguration.json
```

You should see the server logs in the console, indicating it is running and ready to accept WebSocket connections.

```text
Loading configuration file var/config/TIC2WebSocketConfiguration.json
TIC2WebSocket initialized
TIC2WebSocket starting
Starting TICCore
Starting TIC2WebSocket Netty server on localhost:19584
TIC2WebSocket Netty server started successfully on localhost:19584
TIC2WebSocket started
```

### Windows

Once the TIC2WebSocket archive has been generated, extract it into a directory of your choice.

Set the `APPLICATION_HOME` and `VERSION` environment variables to indicate the installation folder path and the TIC2WebSocket version.

```powershell
$env:APPLICATION_HOME = "C:\path\to\folder"
$env:VERSION = "1.0.0"
```

```powershell
Unzip-File -Path TIC2WebSocket-$env:VERSION-bin.zip -DestinationPath $env:APPLICATION_HOME
cd $env:APPLICATION_HOME
```

Show the TIC2WebSocket launcher help with:

```powershell
.\TIC2WebSocket.bat --help
```

You should see the help options, confirming the server is ready to be started.

Stop the server with Ctrl+C.

To start the TIC2WebSocket server, use:

```powershell
.\TIC2WebSocket.bat --verbose=INFO --configFile=var/config/TIC2WebSocketConfiguration.json
```

You should see the server logs in the console, indicating it is running and ready to accept WebSocket connections.

```text
Loading configuration file var/config/TIC2WebSocketConfiguration.json
TIC2WebSocket initialized
TIC2WebSocket starting
Starting TICCore
Starting TIC2WebSocket Netty server on localhost:19584
TIC2WebSocket Netty server started successfully on localhost:19584
TIC2WebSocket started
```

## Test the WebSocket connection

Once the TIC2WebSocket server is started, you can test the WebSocket connection using the WebSocket client embedded in the documentation. Follow the instructions in [WebSocket Tester](ws-tester.md) to connect to the TIC2WebSocket server and receive TIC data in real time.
