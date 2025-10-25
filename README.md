# FleetWave (Spring Boot Multi-tenant MVP)

## Quick start
1. Start Postgres: `docker compose up -d db`
2. Build & run: `make run` (or `mvn -pl fleetwave-web spring-boot:run`)
3. Open: http://localhost:8080/ (admin) and http://localhost:8080/swagger-ui/index.html

### Default users (in-memory)
- `admin` / `admin`
- `dispatcher` / `dispatcher`

## API
- Assignments: `POST /api/v1/assignments` and `POST /api/v1/assignments/{id}/return`
- OpenAPI: `/v3/api-docs` & Swagger UI

## Multi-tenancy
This scaffold uses a request filter to set a tenant (dev default UUID). Replace with subdomain/header → DB lookup.

## Build container
```
make package
docker compose up --build
```

## Next steps
- Implement tenant resolver (subdomain → tenant UUID)
- Flesh out Request/Approval workflow, Alerts engine, Notifications providers
- Add portal build under `fleetwave-portal` (separate)

### Dev portal
Run `cd fleetwave-portal && npm i && npm run dev` (port 5173). Configure CORS if using separate origin.
