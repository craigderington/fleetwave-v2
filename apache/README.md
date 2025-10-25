# FleetWave Apache Helper Scripts

Two helper scripts for Debian/Ubuntu hosts:

- `install_apache_proxy.sh` — installs Apache modules, copies the provided vhost configs, enables sites, (optionally) obtains Let's Encrypt certs, and reloads Apache.
- `deploy_portal.sh` — builds the React portal and syncs the `dist/` to `/var/www/portal` with correct permissions.

> Run these as root (or with sudo). Assumes you already downloaded the Apache vhost bundle (`apache-fleetwave-reverse-proxy.zip`) and extracted it.

## Usage

### 1) Install & enable Apache reverse proxy
```bash
sudo ./install_apache_proxy.sh   --conf-dir /path/to/apache-fleetwave-reverse-proxy/sites-available   --certbot auto   --domains "fleetwave.org,admin.fleetwave.org,api.fleetwave.org,portal.fleetwave.org"
# For wildcard via DNS challenge:
# sudo ./install_apache_proxy.sh --conf-dir /path/to/... --certbot wildcard --domain-root fleetwave.org
```

### 2) Deploy portal static build (Option A)
```bash
sudo ./deploy_portal.sh --portal-root /path/to/repo/fleetwave-portal
# or if you already have a dist/:
# sudo ./deploy_portal.sh --dist /path/to/dist
```
