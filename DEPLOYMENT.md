# FleetWave Deployment Guide

Complete deployment guide for FleetWave radio fleet management system.

## Overview

FleetWave consists of three main components:
1. **Spring Boot Backend** - REST API and Thymeleaf pages (port 8080)
2. **React-Admin Dashboard** - Administrative interface (port 3001 in dev)
3. **React Portal** - End-user self-service (port 3000 in dev)

## Development Environment

### Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 18+
- PostgreSQL 16 (via Docker or local install)
- Docker & Docker Compose (for PostgreSQL)

### 1. Start Database

```bash
# Start PostgreSQL via Docker Compose
docker compose up -d

# Verify database is running
docker ps
```

This starts PostgreSQL with:
- Database: `fleetwave`
- User: `fleet`
- Password: `fleet`
- Port: `5432`

### 2. Build and Run Backend

```bash
# Build all modules
mvn clean install -DskipTests

# Run Spring Boot application
cd fleetwave-web
mvn spring-boot:run
```

Backend will be available at: `http://localhost:8080`

**Endpoints:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`
- Health Check: `http://localhost:8080/actuator/health`
- Login Page: `http://localhost:8080/login`
- Status Page: `http://localhost:8080/status`

### 3. Run React-Admin Dashboard

```bash
cd fleetwave-admin
npm install
npm run dev
```

Admin dashboard will be available at: `http://localhost:3001`

**Default Credentials:**
- Username: `admin`
- Password: `password`

### 4. Run React Portal (Optional)

```bash
cd fleetwave-portal
npm install
npm run dev
```

Portal will be available at: `http://localhost:3000`

---

## Production Deployment

### Build Production Bundle

#### 1. Build Frontend Applications

```bash
# Build React-Admin
cd fleetwave-admin
npm run build

# Build React Portal
cd ../fleetwave-portal
npm run build
```

#### 2. Copy Frontends to Spring Boot Static Resources

```bash
# From project root
cp -r fleetwave-admin/dist/* fleetwave-web/src/main/resources/static/admin/
cp -r fleetwave-portal/dist/* fleetwave-web/src/main/resources/static/portal/
```

#### 3. Build Spring Boot with Embedded Frontends

```bash
mvn clean package -DskipTests
```

Output JAR: `fleetwave-web/target/fleetwave-web-0.4.0-SNAPSHOT.jar`

### Deploy Production JAR

```bash
# Run the JAR file
java -jar fleetwave-web/target/fleetwave-web-0.4.0-SNAPSHOT.jar \
  -Duser.timezone=America/New_York
```

**All interfaces served from single app:**
- Landing Page: `http://localhost:8080/`
- Admin Dashboard: `http://localhost:8080/admin/`
- User Portal: `http://localhost:8080/portal/`
- API: `http://localhost:8080/api/*`

---

## Database Migrations

FleetWave uses Flyway for database schema management.

### Migration Files

Located in: `fleetwave-web/src/main/resources/db/migration/`

- `V1__initial_schema.sql` - Base tables
- `V2__align_assignment_status_check.sql` - Status constraint fix
- `V3__fix_assignment_status_check.sql` - Assignment status update
- `V4__add_person_authentication.sql` - Authentication fields

### Run Migrations Manually

```bash
cd fleetwave-web
mvn flyway:migrate
```

### Check Migration Status

```bash
mvn flyway:info
```

---

## Configuration

### Application Properties

Edit `fleetwave-web/src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fleetwave
    username: fleet
    password: fleet

  jpa:
    hibernate:
      ddl-auto: validate  # Use Flyway for schema management
    show-sql: false

  flyway:
    enabled: true
    baseline-on-migrate: true
```

### Environment Variables

Override configuration using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/fleetwave
export SPRING_DATASOURCE_USERNAME=fleet_prod
export SPRING_DATASOURCE_PASSWORD=secure_password

java -jar fleetwave-web-0.4.0-SNAPSHOT.jar
```

---

## Security Configuration

### Default Users

Created by V4 migration:

- **Admin User**
  - Username: `admin`
  - Password: `password` (BCrypt hashed)
  - Roles: ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
  - Tenant: `ocps`

### Change Admin Password

```sql
-- Hash new password with BCrypt (strength 10)
-- Example: 'newpassword' becomes:
-- $2a$10$dXJ3SW6G7P1/nzI5qfJ3seKdO.aX7fkL7kQ4zwvPxKC7bB2oC/Q4q

UPDATE persons
SET password_hash = '$2a$10$dXJ3SW6G7P1/nzI5qfJ3seKdO.aX7fkL7kQ4zwvPxKC7bB2oC/Q4q'
WHERE username = 'admin' AND tenant_id = 'ocps';
```

### Create New Users

Via Admin Dashboard or SQL:

```sql
INSERT INTO persons (
  id, tenant_id, first_name, last_name, email,
  username, password_hash, roles, enabled,
  created_at, updated_at
) VALUES (
  gen_random_uuid(),
  'ocps',
  'John',
  'Doe',
  'john.doe@ocps.gov',
  'john.doe',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'ROLE_USER',
  true,
  NOW(),
  NOW()
);
```

---

## Multi-Tenancy

FleetWave supports multi-tenant deployment using shared schema with `tenant_id` column.

### Add New Tenant

```sql
-- Insert tenant data with tenant_id prefix
-- Example: 'cityX' tenant

INSERT INTO persons (..., tenant_id, ...) VALUES (..., 'cityX', ...);
INSERT INTO radios (..., tenant_id, ...) VALUES (..., 'cityX', ...);
```

### Tenant Resolution

- **API Requests**: via `X-Tenant` header
- **Web Requests**: via session attribute (set during login)
- **Default**: `ocps` for demo purposes

---

## Monitoring & Health

### Actuator Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Metrics
curl http://localhost:8080/actuator/metrics
```

### Dashboard Statistics

Admin dashboard fetches real-time stats from:
- `GET /api/stats/overview` - Overall statistics
- `GET /api/stats/radios/by-status` - Radio distribution

---

## Troubleshooting

### Database Connection Issues

```bash
# Check PostgreSQL is running
docker ps

# Check connection
psql -h localhost -U fleet -d fleetwave -c "SELECT version();"
```

### Build Failures

```bash
# Clean Maven cache
mvn clean

# Clean npm cache
cd fleetwave-admin && npm cache clean --force
cd fleetwave-portal && npm cache clean --force
```

### Port Conflicts

```bash
# Check if port 8080 is in use
lsof -i :8080

# Kill process on port 8080
kill -9 $(lsof -t -i:8080)
```

---

## Performance Optimization

### Production Recommendations

1. **Database Connection Pool**
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

2. **Enable Caching**
```yaml
spring:
  cache:
    type: caffeine
```

3. **Production Profile**
```bash
java -jar fleetwave-web.jar --spring.profiles.active=prod
```

---

## Backup & Recovery

### Database Backup

```bash
# Backup database
pg_dump -h localhost -U fleet fleetwave > fleetwave-backup.sql

# Restore database
psql -h localhost -U fleet fleetwave < fleetwave-backup.sql
```

### Application Backup

- JAR file: `fleetwave-web/target/fleetwave-web-0.4.0-SNAPSHOT.jar`
- Configuration: `application.yml`
- Database dump

---

## Support

For issues and questions:
- GitHub Issues: https://github.com/your-org/fleetwave/issues
- Documentation: See README.md and ARCHITECTURE.md

---

**Version:** 0.4.0-SNAPSHOT
**Last Updated:** 2025-10-29
