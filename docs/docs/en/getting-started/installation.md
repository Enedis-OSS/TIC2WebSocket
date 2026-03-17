# Installation

## Build the project

### Clone the repository

```bash
git clone https://github.com/Enedis-OSS/TIC2WebSocket.git
```

### Run the Maven build

```bash
cd TIC2WebSocket
mvn clean package
```

### Verify generated artifacts

```bash
cd target
ls
```

You should see the generated deliverables, including `TIC2WebSocket-VERSION-bin.zip` or `TIC2WebSocket-VERSION-bin.tar.gz`.

## Run the documentation locally

### Go to the `docs/` folder

```bash
cd docs
```

### Start the documentation Docker services

```bash
docker-compose up --build
```

### Open the documentation

```text
http://localhost:8000
```

You should see the TIC2WebSocket documentation in your browser.
