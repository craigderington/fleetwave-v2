#!/usr/bin/env bash
set -euo pipefail

# FleetWave bootstrap: downloads Apache vhost bundle + helper scripts and runs setup interactively.
# Usage (one-liner):
#   curl -fsSL https://example.com/fleetwave_bootstrap.sh | bash -s -- \
#     --vhost-url https://example.com/apache-fleetwave-reverse-proxy.zip \
#     --scripts-url https://example.com/fleetwave-apache-scripts.zip
#
# Or run locally: ./fleetwave_bootstrap.sh --vhost-zip /path/to/apache-fleetwave-reverse-proxy.zip --scripts-zip /path/to/fleetwave-apache-scripts.zip

VHOST_URL=""
SCRIPTS_URL=""
VHOST_ZIP=""
SCRIPTS_ZIP=""
PORTAL_ROOT=""
CERTBOT_MODE="auto"   # auto|wildcard|none
DOMAINS=""
DOMAIN_ROOT=""
NON_INTERACTIVE="false"

usage() {
  cat <<EOF
FleetWave bootstrap

Flags:
  --vhost-url <url>        URL to apache-fleetwave-reverse-proxy.zip
  --scripts-url <url>      URL to fleetwave-apache-scripts.zip
  --vhost-zip <path>       Local path to apache-fleetwave-reverse-proxy.zip
  --scripts-zip <path>     Local path to fleetwave-apache-scripts.zip
  --portal-root <path>     Optional path to fleetwave-portal for static deploy
  --certbot <auto|wildcard|none>  Default: auto
  --domains "<csv>"        Domains for certbot auto mode (csv)
  --domain-root <root>     Root domain for wildcard mode (e.g., fleetwave.org)
  --yes                    Non-interactive (use provided flags)
  -h|--help                Show this help
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --vhost-url) VHOST_URL="$2"; shift 2;;
    --scripts-url) SCRIPTS_URL="$2"; shift 2;;
    --vhost-zip) VHOST_ZIP="$2"; shift 2;;
    --scripts-zip) SCRIPTS_ZIP="$2"; shift 2;;
    --portal-root) PORTAL_ROOT="$2"; shift 2;;
    --certbot) CERTBOT_MODE="$2"; shift 2;;
    --domains) DOMAINS="$2"; shift 2;;
    --domain-root) DOMAIN_ROOT="$2"; shift 2;;
    --yes) NON_INTERACTIVE="true"; shift;;
    -h|--help) usage; exit 0;;
    *) echo "Unknown arg: $1"; usage; exit 1;;
  esac
done

need() { command -v "$1" >/dev/null 2>&1 || { echo "Installing $1..."; apt-get update -y && apt-get install -y "$1"; }; }

echo "==> Checking prerequisites (curl, unzip, rsync)"
need curl
need unzip
need rsync

TMP="$(mktemp -d)"
cleanup() { rm -rf "$TMP"; }
trap cleanup EXIT

# Download or use local zips
if [[ -z "$VHOST_ZIP" ]]; then
  if [[ -n "$VHOST_URL" ]]; then
    echo "==> Downloading vhost bundle"
    curl -fsSL "$VHOST_URL" -o "$TMP/apache.zip"
    VHOST_ZIP="$TMP/apache.zip"
  else
    if [[ "$NON_INTERACTIVE" == "true" ]]; then echo "ERROR: --vhost-url or --vhost-zip required in non-interactive mode"; exit 1; fi
    read -rp "Enter path or URL to apache-fleetwave-reverse-proxy.zip: " VHOST_INPUT
    if [[ "$VHOST_INPUT" =~ ^https?:// ]]; then curl -fsSL "$VHOST_INPUT" -o "$TMP/apache.zip"; VHOST_ZIP="$TMP/apache.zip"; else VHOST_ZIP="$VHOST_INPUT"; fi
  fi
fi
if [[ -z "$SCRIPTS_ZIP" ]]; then
  if [[ -n "$SCRIPTS_URL" ]]; then
    echo "==> Downloading helper scripts"
    curl -fsSL "$SCRIPTS_URL" -o "$TMP/scripts.zip"
    SCRIPTS_ZIP="$TMP/scripts.zip"
  else
    if [[ "$NON_INTERACTIVE" == "true" ]]; then echo "ERROR: --scripts-url or --scripts-zip required in non-interactive mode"; exit 1; fi
    read -rp "Enter path or URL to fleetwave-apache-scripts.zip: " SCRIPTS_INPUT
    if [[ "$SCRIPTS_INPUT" =~ ^https?:// ]]; then curl -fsSL "$SCRIPTS_INPUT" -o "$TMP/scripts.zip"; SCRIPTS_ZIP="$TMP/scripts.zip"; else SCRIPTS_ZIP="$SCRIPTS_INPUT"; fi
  fi
fi

echo "==> Extracting bundles"
mkdir -p "$TMP/apache" "$TMP/scripts"
unzip -q "$VHOST_ZIP" -d "$TMP/apache"
unzip -q "$SCRIPTS_ZIP" -d "$TMP/scripts"

CONF_DIR="$(find "$TMP/apache" -type d -name 'sites-available' | head -n1)"
if [[ -z "$CONF_DIR" ]]; then echo "ERROR: could not locate sites-available in vhost bundle"; exit 1; fi

INSTALL_SH="$(find "$TMP/scripts" -type f -name 'install_apache_proxy.sh' | head -n1)"
DEPLOY_SH="$(find "$TMP/scripts" -type f -name 'deploy_portal.sh' | head -n1)"
chmod +x "$INSTALL_SH" "$DEPLOY_SH"

if [[ "$NON_INTERACTIVE" != "true" ]]; then
  echo "==> Configure certbot:"
  read -rp "Use certbot mode [auto|wildcard|none] (default auto): " ans; CERTBOT_MODE="${ans:-$CERTBOT_MODE}"
  if [[ "$CERTBOT_MODE" == "auto" ]]; then
    read -rp "Enter CSV domains (e.g., fleetwave.org,admin.fleetwave.org,api.fleetwave.org,portal.fleetwave.org): " DOMAINS
  elif [[ "$CERTBOT_MODE" == "wildcard" ]]; then
    read -rp "Enter domain root (e.g., fleetwave.org): " DOMAIN_ROOT
  fi
  read -rp "Deploy portal static build now? [y/N]: " depnow
  if [[ "${depnow,,}" == "y" ]]; then
    read -rp "Enter path to fleetwave-portal directory: " PORTAL_ROOT
  fi
fi

echo "==> Running Apache installer"
if [[ "$CERTBOT_MODE" == "auto" ]]; then
  sudo "$INSTALL_SH" --conf-dir "$CONF_DIR" --certbot auto --domains "${DOMAINS:-fleetwave.org,admin.fleetwave.org,api.fleetwave.org,portal.fleetwave.org}"
elif [[ "$CERTBOT_MODE" == "wildcard" ]]; then
  sudo "$INSTALL_SH" --conf-dir "$CONF_DIR" --certbot wildcard --domain-root "${DOMAIN_ROOT:-fleetwave.org}"
else
  sudo "$INSTALL_SH" --conf-dir "$CONF_DIR" --certbot none
fi

if [[ -n "${PORTAL_ROOT:-}" ]]; then
  echo "==> Deploying portal"
  sudo "$DEPLOY_SH" --portal-root "$PORTAL_ROOT"
else
  echo "==> Skipping portal deploy (no --portal-root provided)"
fi

echo "Bootstrap complete. 🎉"
