package org.fleetwave.web.bootstrap;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.fleetwave.infra.tenant.TenantResolver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile({"default","dev"})
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

  private final org.springframework.jdbc.core.JdbcTemplate jdbc;
  private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
  private final UserRepository users;
  private final RoleRepository roles;
  private final UserRoleRepository userRoles;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // Seed tenant 'ocps'
    UUID ocpsId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    jdbc.update("insert into tenant(id,key,name) values (?,?,?) on conflict do nothing",
      ocpsId, "ocps", "Orange County Public Schools");

    // Seed roles
    var adminRole = roles.save(Role.builder().id(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")).tenantId(ocpsId).name("ADMIN").build());
    var dispRole  = roles.save(Role.builder().id(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000002")).tenantId(ocpsId).name("DISPATCHER").build());
    var mgrRole   = roles.save(Role.builder().id(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000003")).tenantId(ocpsId).name("MANAGER").build());

    // Seed admin user
    var admin = users.findByEmail("admin@ocps.fleetwave.org").orElseGet(() -> {
      var u = User.builder()
        .id(UUID.fromString("bbbbbbbb-0000-0000-0000-000000000001"))
        .tenantId(ocpsId)
        .email("admin@ocps.fleetwave.org")
        .name("OCPS Admin")
        .passwordHash(enc.encode("ChangeMe123!"))
        .enabled(true).build();
      return users.save(u);
    });

    userRoles.save(UserRole.builder().id(UUID.randomUUID()).tenantId(ocpsId).user(admin).role(adminRole).build());
    userRoles.save(UserRole.builder().id(UUID.randomUUID()).tenantId(ocpsId).user(admin).role(dispRole).build());
    userRoles.save(UserRole.builder().id(UUID.randomUUID()).tenantId(ocpsId).user(admin).role(mgrRole).build());
  }
}
