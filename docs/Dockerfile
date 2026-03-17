FROM squidfunk/mkdocs-material

# Required because this repository is configured with Git LFS hooks.
# `mkdocs gh-deploy` triggers a `git push` which runs the pre-push hook.
RUN apk add --no-cache git-lfs

COPY requirements.txt .
RUN pip install -r requirements.txt
