package org.fleetwave.web.bootstrap;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SeedRunner implements ApplicationRunner {
  @Value("${seed.ocps:false}") boolean seedOcps;

  private final JdbcTemplate jdbc;
  private final UserRepository users;
  private final RoleRepository roles;
  private final UserRoleRepository userRoles;

  @Override public void run(ApplicationArguments args) {
    if (!seedOcps) return;
    var exists = jdbc.queryForObject("select count(1) from seed_log where key='ocps'", Integer.class);
    if (exists != null && exists > 0) return; // already seeded

    UUID ocpsId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    jdbc.update("insert into tenant(id,key,name) values (?,?,?) on conflict do nothing", ocpsId, "ocps","Orange County Public Schools");

    var adminRole = roles.save(Role.builder().id(UUID.randomUUID()).tenantId(ocpsId).name("ADMIN").build());
    var dispRole  = roles.save(Role.builder().id(UUID.randomUUID()).tenantId(ocpsId).name("DISPATCHER").build());
    var mgrRole   = roles.save(Role.builder().id(UUID.randomUUID()).tenantId(ocpsId).name("MANAGER").build());

    var enc = new BCryptPasswordEncoder();
    var admin = users.findByEmail("admin@ocps.fleetwave.org").orElseGet(() ->
      users.save(User.builder()
        .id(UUID.randomUUID()).tenantId(ocpsId).email("admin@ocps.fleetwave.org").name("OCPS Admin")
        .passwordHash(enc.encode("ChangeMe123!")).enabled(true).build())
    );
    userRoles.save(UserRole.builder().id(UUID.randomUUID()).tenantId(ocpsId).user(admin).role(adminRole).build());
    userRoles.save(UserRole.builder().id(UUID.randomUUID()).tenantId(ocpsId).user(admin).role(dispRole).build());
    userRoles.save(UserRole.builder().id(UUID.randomUUID()).tenantId(ocpsId).user(admin).role(mgrRole).build());

    jdbc.update("insert into seed_log(id, tenant_id, key) values (?,?,?)",
      UUID.randomUUID(), ocpsId, "ocps");
  }
}
