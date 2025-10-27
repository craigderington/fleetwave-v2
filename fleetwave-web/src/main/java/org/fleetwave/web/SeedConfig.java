package org.fleetwave.web;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedConfig {

    @Bean
    CommandLineRunner seedData(RadioRepository radios,
                               PersonRepository people,
                               WorkgroupRepository workgroups,
                               AssignmentRepository assignments) {
        return args -> {
            String tenant = "ocps";

            if (radios.count() == 0) {
                Radio r = new Radio();
                r.setId(UUID.randomUUID());
                r.setSerial("XPR-12345");
                r.setModel("Motorola XPR");
                r.setStatus("AVAILABLE");
                r.setCreatedAt(OffsetDateTime.now());
                r.setUpdatedAt(OffsetDateTime.now());
                r.setTenantId(tenant);
                radios.save(r);
            }

            if (people.count() == 0) {
                Person p = new Person();
                p.setId(UUID.randomUUID());
                p.setFirstName("Ada");
                p.setLastName("Lovelace");
                p.setEmail("ada@ocps.example");
                p.setCreatedAt(OffsetDateTime.now());
                p.setUpdatedAt(OffsetDateTime.now());
                p.setTenantId(tenant);
                people.save(p);
            }

            if (workgroups.count() == 0) {
                Workgroup g = new Workgroup();
                g.setId(UUID.randomUUID());
                g.setName("Dispatch");
                g.setCreatedAt(OffsetDateTime.now());
                g.setUpdatedAt(OffsetDateTime.now());
                g.setTenantId(tenant);
                workgroups.save(g);
            }

            if (assignments.count() == 0) {
                Radio r = radios.findAll().get(0);
                Person p = people.findAll().get(0);
                Assignment a = new Assignment();
                a.setId(UUID.randomUUID());
                a.setRadio(r);
                a.setAssigneePerson(p);
                a.setStartAt(OffsetDateTime.now());
                a.setExpectedEnd(OffsetDateTime.now().plusDays(7));
                a.setStatus(Assignment.Status.ASSIGNED);
                a.setCreatedAt(OffsetDateTime.now());
                a.setUpdatedAt(OffsetDateTime.now());
                a.setTenantId(tenant);
                assignments.save(a);
            }
        };
    }
}
