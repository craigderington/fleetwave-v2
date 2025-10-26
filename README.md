FleetWave

FleetWave is a multi-tenant fleet & radio management system built on Spring Boot 3, PostgreSQL, and Thymeleaf/REST with an alerts & notifications pipeline (SES/Twilio ready). It models people/workgroups, radio inventory, assignment lifecycles, maintenance work orders, and alert rules (e.g., overdue returns).
Modules

fleetwave/
├─ fleetwave-domain   # JPA entities (Lombok-free) + repositories
├─ fleetwave-app      # Services, schedulers (OverdueScanner), workers (NotificationWorker), notification SPI
├─ fleetwave-infra    # Infra providers (SES email, Twilio SMS), conditional beans
└─ fleetwave-web      # Spring Boot app, REST controllers, Swagger, Actuator, dev fallbacks, seed data

Tech Stack

    Java 21, Spring Boot 3.3.x, Spring Data JPA (Hibernate 6)

    PostgreSQL 16 (Docker Compose), H2 (dev profile)

    OpenAPI/Swagger UI via springdoc

    Notifications: SPI with SES (email) & Twilio (SMS) providers (toggle by properties)

    Scheduling: Overdue assignment scanner (raises alerts)

    Actuator for health & diagnostics

Quick Start
1) Prerequisites

    Java 21+

    Maven 3.9+

    Docker & Docker Compose

2) Start Postgres (recommended for dev)

From repo root:

docker compose up -d

This starts Postgres with:

    DB: fleetwave

    USER: fleet

    PASS: fleet

3) Build everything

mvn -DskipTests clean install

4) Run the app (Postgres)

cd fleetwave-web
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=America/New_York"

    We explicitly set the JVM timezone to avoid legacy timezone aliases (see Timezone below).

5) Open Swagger / Health

    Swagger UI: http://localhost:8080/swagger-ui.html

    Docs JSON: http://localhost:8080/v3/api-docs

    Health: http://localhost:8080/actuator/health

Configuration
Application properties (Postgres default)

fleetwave-web/src/main/resources/application.yml:

server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fleetwave?options=-c%20TimeZone=America/New_York
    username: fleet
    password: fleet
    hikari:
      initializationFailTimeout: 0
      connectionTimeout: 30000

  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    properties:
      hibernate.jdbc.time_zone: UTC

springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    display-request-duration: true

notif:
  email:
    provider: ses       # values: ses | dev
  sms:
    provider: twilio    # values: twilio | dev

management:
  endpoints:
    web:
      exposure:
        include: health,info,mappings,beans

Dev profile (H2)

Run with the H2 profile if you want zero external deps:

cd fleetwave-web
mvn spring-boot:run -Dspring-boot.run.profiles=h2

Seed Data

On an empty DB, the app seeds:

    One Radio (APX-001)

    One Workgroup (Dispatch)

    One active overdue Assignment (to trigger an alert)

    One AlertRule for overdue detection

You can verify via Swagger (/api/*) or curl:

curl -s http://localhost:8080/api/radios | jq .
curl -s http://localhost:8080/api/assignments | jq .
# Scanner runs every ~60s
curl -s http://localhost:8080/api/alerts | jq .

REST Endpoints (high-level)

    GET /ping — health check (pong)

    GET /api/radios / POST /api/radios

    GET /api/assignments / POST /api/assignments / POST /api/assignments/{id}/return

    GET /api/requests / POST /api/requests / POST /api/requests/{id}/approve / POST /api/requests/{id}/reject

    GET /api/workorders / POST /api/workorders / POST /api/workorders/{id}/status?status=...

    GET /api/alerts

Swagger lists all methods, request/response models, and sample payloads.
Notifications

    SPI: EmailSender, SmsSender, EmailMessage

    Providers:

        SES when notif.email.provider=ses (configure AWS creds & region)

        Twilio when notif.sms.provider=twilio (configure account SID/token)

        Dev fallbacks auto-enable when providers are not configured (prevents missing-bean errors and logs outbound messages instead of sending).

Enable SES/Twilio by adding environment variables or properties (examples):

# SES
export AWS_REGION=us-east-1
export AWS_ACCESS_KEY_ID=...
export AWS_SECRET_ACCESS_KEY=...
# Twilio
export TWILIO_ACCOUNT_SID=...
export TWILIO_AUTH_TOKEN=...

And in application.yml:

notif:
  email:
    provider: ses
  sms:
    provider: twilio

Timezone

Some Postgres setups reject legacy timezone names like US/Eastern.
We enforce:

    JVM default timezone at runtime: -Duser.timezone=America/New_York (or UTC)

    JDBC session timezone: hibernate.jdbc.time_zone: UTC

    Postgres GUC via JDBC options: ?options=-c%20TimeZone=America/New_York (optional but recommended)

Sanity checks:

# JVM
jshell <<'EOF'
import java.util.*;
System.out.println(TimeZone.getDefault().getID());
EOF

# Postgres
psql "postgresql://fleet:fleet@localhost:5432/fleetwave" -c "SHOW TimeZone;"

Docker: Postgres

docker-compose.yml (excerpt):

version: '3.8'
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: fleetwave
      POSTGRES_USER: fleet
      POSTGRES_PASSWORD: fleet
      TZ: America/New_York
    command: ["postgres", "-c", "timezone=America/New_York"]
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U fleet -d fleetwave || exit 1"]
      interval: 3s
      timeout: 3s
      retries: 20
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:

If you change credentials after first start, remove the existing volume:

docker compose down -v
docker compose up -d

Apache Reverse Proxy (optional)

ops/apache/fleetwave.conf provides an example SSL reverse proxy to the app at :8080.
Enable SSL certs and the site in Apache, then reload.
Formatting & Conventions

We ship Lombok-free code (explicit getters/setters/builders) and enforce formatting with Spotless (Google Java Format). To (re)format:

mvn spotless:apply

Recommended .editorconfig at repo root:

root = true

[*.java]
charset = utf-8
indent_style = space
indent_size = 2
insert_final_newline = true
trim_trailing_whitespace = true

Troubleshooting

Password auth fails for user fleet

    Likely stale Docker volume with old password. Recreate:

docker compose down -v && docker compose up -d

Or create role/db in psql:

    CREATE ROLE fleet WITH LOGIN PASSWORD 'fleet';
    CREATE DATABASE fleetwave OWNER fleet;
    GRANT ALL PRIVILEGES ON DATABASE fleetwave TO fleet;

FATAL: invalid value for parameter "TimeZone": "US/Eastern"

    Set JVM timezone to a valid IANA zone:

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=America/New_York"

    Optionally add ?options=-c%20TimeZone=America/New_York to JDBC URL.

Swagger shows nothing or 404

    Confirm app started and mappings exist:

http://localhost:8080/actuator/mappings

Swagger UI is served at:

    http://localhost:8080/swagger-ui.html

    (redirects to /swagger-ui/index.html)

Startup fails with “Unable to determine Dialect without JDBC metadata”

    It’s a cascade from connection failure. Fix DB connectivity (see above).

Roadmap

    Flyway migrations (baseline + versioned changes)

    OIDC (keep in-app auth as fallback)

    Tenant resolution from subdomain ({tenant}.fleetwave.org)

    SES/Twilio production wiring + delivery health endpoints

    CI/CD with GitHub Actions (build, test, Docker publish)

    Observability (Micrometer → Prometheus/Grafana)

License

TBD
