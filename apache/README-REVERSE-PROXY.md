# Apache2 Reverse Proxy for FleetWave

These vhosts terminate TLS and reverse-proxy to backend apps (Spring Boot on 127.0.0.1:8080; Portal either local static or CloudFront).

## Modules required
```
a2enmod ssl proxy proxy_http proxy_wstunnel headers rewrite http2 deflate brotli
```
*(brotli requires `libbrotli` packages; otherwise omit)*

## Install
1. Copy `sites-available/*.conf` to `/etc/apache2/sites-available/`
2. `a2ensite fleetwave-80-redirect.conf fleetwave-admin.conf fleetwave-api.conf fleetwave-portal.conf fleetwave-tenant-wildcard.conf`
3. Place certs from Let's Encrypt at the paths used below (or run certbot):
   ```bash
   apt-get install -y certbot python3-certbot-apache
   certbot --apache -d fleetwave.org -d admin.fleetwave.org -d api.fleetwave.org -d portal.fleetwave.org -d '*.fleetwave.org'
   ```
   > Wildcard requires DNS challenge: `certbot -d '*.fleetwave.org' -d fleetwave.org --manual --preferred-challenges dns`
4. `systemctl reload apache2`

## Backend targets (edit if different)
- App (admin+api+tenants): `http://127.0.0.1:8080`
- Portal:
  - Option A (static build): `/var/www/portal` (put portal `dist/` here)
  - Option B (CloudFront): proxy to `https://portal.fleetwave.org` (set `ProxyPass` accordingly)

## Notes
- HSTS is enabled (preload). Ensure all subdomains use HTTPS before enabling preload.
- Security headers and compression enabled.
- WebSockets pass-through enabled for future endpoints.
