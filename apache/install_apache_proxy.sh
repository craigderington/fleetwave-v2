#!/usr/bin/env bash
set -euo pipefail

CONF_DIR=""
CERTBOT_MODE="none" # none|auto|wildcard
DOMAINS=""
DOMAIN_ROOT=""
SITES=("fleetwave-80-redirect.conf" "fleetwave-admin.conf" "fleetwave-api.conf" "fleetwave-portal.conf" "fleetwave-tenant-wildcard.conf")

usage() {
  cat <<EOF
Usage: $0 --conf-dir <dir> [--certbot none|auto|wildcard] [--domains "a.com,admin.a.com,api.a.com,portal.a.com"] [--domain-root a.com]

Examples:
  $0 --conf-dir /tmp/apache/sites-available --certbot auto --domains "fleetwave.org,admin.fleetwave.org,api.fleetwave.org,portal.fleetwave.org"
  $0 --conf-dir /tmp/apache/sites-available --certbot wildcard --domain-root fleetwave.org
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --conf-dir) CONF_DIR="$2"; shift 2;;
    --certbot) CERTBOT_MODE="$2"; shift 2;;
    --domains) DOMAINS="$2"; shift 2;;
    --domain-root) DOMAIN_ROOT="$2"; shift 2;;
    -h|--help) usage; exit 0;;
    *) echo "Unknown arg: $1"; usage; exit 1;;
  esac
done

if [[ -z "$CONF_DIR" ]]; then echo "ERROR: --conf-dir is required"; usage; exit 1; fi
if [[ ! -d "$CONF_DIR" ]]; then echo "ERROR: conf dir not found: $CONF_DIR"; exit 1; fi

echo "==> Installing Apache and modules"
apt-get update -y
apt-get install -y apache2 libapache2-mod-brotli || true
a2enmod ssl proxy proxy_http proxy_wstunnel headers rewrite http2 deflate brotli || true

echo "==> Copying vhosts from $CONF_DIR"
cp -v "$CONF_DIR"/*.conf /etc/apache2/sites-available/

echo "==> Enabling sites"
a2ensite "${SITES[@]}"

if [[ "$CERTBOT_MODE" != "none" ]]; then
  echo "==> Installing certbot"
  apt-get install -y certbot python3-certbot-apache
  if [[ "$CERTBOT_MODE" == "auto" ]]; then
    if [[ -z "$DOMAINS" ]]; then echo "ERROR: --domains required for certbot auto"; exit 1; fi
    IFS=',' read -r -a arr <<< "$DOMAINS"
    flags=()
    for d in "${arr[@]}"; do flags+=(-d "$d"); done
    certbot --apache "${flags[@]}" --non-interactive --agree-tos -m admin@"${arr[0]}" || true
  elif [[ "$CERTBOT_MODE" == "wildcard" ]]; then
    if [[ -z "$DOMAIN_ROOT" ]]; then echo "ERROR: --domain-root required for wildcard"; exit 1; fi
    echo "==> Requesting wildcard cert (manual DNS challenge needed)"
    certbot -d "*.$DOMAIN_ROOT" -d "$DOMAIN_ROOT" --manual --preferred-challenges dns certonly
    echo "Place resulting certs under /etc/letsencrypt/live/$DOMAIN_ROOT and ensure vhosts reference them."
  fi
fi

echo "==> Test config & reload"
apache2ctl configtest
systemctl reload apache2
echo "Done."
