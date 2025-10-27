package org.fleetwave.web;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.fleetwave.domain.Person;
import org.fleetwave.domain.Radio;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.domain.repo.RadioRepository;
import org.fleetwave.domain.repo.WorkgroupRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("dev")
class SeedConfig {

    @Bean
    CommandLineRunner seedData(
            PersonRepository persons,
            WorkgroupRepository workgroups,
            RadioRepository radios
    ) {
        return args -> doSeed(persons, workgroups, radios);
    }

    @Transactional
    void doSeed(
            PersonRepository persons,
            WorkgroupRepository workgroups,
            RadioRepository radios
    ) {
        // Common constants
        final String TENANT = "ocps";
        final OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        // 1) Seed core lookup entities
        Person alice = upsertPerson(persons, TENANT, "alice@example.com", "Alice", "Anderson", now);
        Person bob   = upsertPerson(persons, TENANT, "bob@example.com",   "Bob",   "Baker",    now);

        Workgroup ops = upsertWorkgroup(workgroups, TENANT, "Ops", now);
        Workgroup it  = upsertWorkgroup(workgroups, TENANT, "IT",  now);

        // 2) Seed radios (avoid duplicate serial collisions)
        Radio r1 = upsertRadio(radios, TENANT, "XPR-12345", "Motorola XPR", Radio.Status.AVAILABLE, now);
        Radio r2 = upsertRadio(radios, TENANT, "APX-9001",  "Motorola APX", Radio.Status.AVAILABLE, now);

        // (Optional) If you seed assignments, use AssignmentService to enforce rules
        // e.g., assignmentService.assignToPerson(TENANT, r1.getId(), alice.getId(), now.plusDays(7));
    }

    private Person upsertPerson(
            PersonRepository persons,
            String tenantId,
            String email,
            String firstName,
            String lastName,
            OffsetDateTime nowUtc
    ) {
        return persons.findByTenantIdAndEmail(tenantId, email).orElseGet(() -> {
            Person p = new Person();
            p.setId(UUID.randomUUID());
            p.setTenantId(tenantId);
            p.setEmail(email);
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setCreatedAt(nowUtc);
            p.setUpdatedAt(nowUtc);
            return persons.save(p);
        });
    }

    private Workgroup upsertWorkgroup(
            WorkgroupRepository workgroups,
            String tenantId,
            String name,
            OffsetDateTime nowUtc
    ) {
        return workgroups.findByTenantIdAndName(tenantId, name).orElseGet(() -> {
            Workgroup g = new Workgroup();
            g.setId(UUID.randomUUID());
            g.setTenantId(tenantId);
            g.setName(name);
            g.setCreatedAt(nowUtc);
            g.setUpdatedAt(nowUtc);
            return workgroups.save(g);
        });
    }

    private Radio upsertRadio(
            RadioRepository radios,
            String tenantId,
            String serial,
            String model,
            Radio.Status status,
            OffsetDateTime nowUtc
    ) {
        return radios.findByTenantIdAndSerial(tenantId, serial).orElseGet(() -> {
            Radio r = new Radio();
            r.setId(UUID.randomUUID());
            r.setTenantId(tenantId);
            r.setSerial(serial);
            r.setModel(model);
            r.setStatus(Radio.Status.ASSIGNED);
            r.setCreatedAt(nowUtc);
            r.setUpdatedAt(nowUtc);
            return radios.save(r);
        });
    }
}

