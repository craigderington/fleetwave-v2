# Fleetwave Tenant Patch (v1)

This patch introduces shared-schema multi-tenancy via a `tenant_id` column and
fixes compile/runtime issues reported:

- Adds `TenantScoped` mapped superclass and updates `Radio`, `Person`, `Workgroup`, `Assignment` to extend it.
- Adds repository methods `findByIdAndTenantId` for Radio/Workgroup/Person.
- Provides `AssignmentRepository.findOverdue(OffsetDateTime)` used by `OverdueScanner`.
- Adds `web` DTO `AssignmentDtos.AssignToPersonRequest` and minimal controllers for assignment/person/membership.
- Fixes seed data to use explicit `assigneePerson` instead of legacy fields.
- Aligns `returnAssignment` call signature.

Drop these files into your repo at the same paths and rebuild:

```bash
mvn -DskipTests clean install
(cd fleetwave-web && mvn spring-boot:run)
```
