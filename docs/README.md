[🇫🇷 Français](README.md) | [🇺🇸 English](README.en.md)

# Configuration pour les développeurs

Depuis le dossier `docs` à la racine du projet :

- Démarrer (serveur PlantUML + serveur MkDocs) : `docker-compose up --build` (ou `docker compose up --build`)
- Arrêter : `docker-compose down`

Puis visiter : http://localhost:8000/tic2websocket/ (la racine `/` redirige vers ce chemin)

## Déployer sur GitHub Pages

La commande `mkdocs gh-deploy` pousse sur la branche `gh-pages` et nécessite une authentification GitHub.

GitHub ne supporte plus l'authentification Git via HTTPS avec le mot de passe du compte : utiliser SSH.

Déploiement en SSH :

- Passer le remote en SSH : `git remote set-url origin git@github.com:Enedis-OSS/TIC2WebSocket.git`
- Générer une clé SSH (déjà ignorée via `var/`) :
	- `mkdir -p ../var/ssh && chmod 700 ../var/ssh`
	- `ssh-keygen -t ed25519 -f ../var/ssh/id_ed25519_tic2websocket -C "tic2websocket-mkdocs-gh-pages" -N ""`
	- `ssh-keyscan -H github.com > ../var/ssh/known_hosts`
- Ajouter la clé publique `../var/ssh/id_ed25519_tic2websocket.pub` sur GitHub (Settings → SSH and GPG keys)
- Déployer : `docker-compose run --rm mkdocs gh-deploy`
