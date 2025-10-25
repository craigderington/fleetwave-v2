#!/usr/bin/env bash
set -euo pipefail

PORTAL_ROOT=""
DIST=""
WWW="/var/www/portal"

usage() {
  cat <<EOF
Usage: $0 [--portal-root <path>] [--dist <path>] [--www /var/www/portal]

Builds the React portal (if --portal-root given) or uses an existing dist/ (if --dist given),
then syncs to /var/www/portal and sets perms.

Examples:
  $0 --portal-root /opt/fleetwave/fleetwave-portal
  $0 --dist /tmp/dist --www /srv/www/portal
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --portal-root) PORTAL_ROOT="$2"; shift 2;;
    --dist) DIST="$2"; shift 2;;
    --www) WWW="$2"; shift 2;;
    -h|--help) usage; exit 0;;
    *) echo "Unknown arg: $1"; usage; exit 1;;
  esac
done

if [[ -n "$PORTAL_ROOT" ]]; then
  echo "==> Building portal at $PORTAL_ROOT"
  apt-get update -y
  apt-get install -y curl
  if ! command -v node >/dev/null 2>&1; then
    echo "Installing Node.js (via NodeSource LTS)"
    curl -fsSL https://deb.nodesource.com/setup_lts.x | bash -
    apt-get install -y nodejs
  fi
  pushd "$PORTAL_ROOT"
  npm ci
  npm run build
  DIST="$PORTAL_ROOT/dist"
  popd
fi

if [[ -z "$DIST" ]]; then echo "ERROR: provide --portal-root or --dist"; usage; exit 1; fi
if [[ ! -d "$DIST" ]]; then echo "ERROR: dist not found: $DIST"; exit 1; fi

echo "==> Syncing to $WWW"
mkdir -p "$WWW"
rsync -av --delete "$DIST"/ "$WWW"/

echo "==> Setting permissions"
chown -R www-data:www-data "$WWW"
find "$WWW" -type d -exec chmod 755 {{}} +
find "$WWW" -type f -exec chmod 644 {{}} +

echo "==> Reload Apache"
systemctl reload apache2 || true
echo "Done."
