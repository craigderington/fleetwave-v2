package org.fleetwave.domain.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {
  List<Person> findAllByTenantId(String tenantId);
  Optional<Person> findByIdAndTenantId(UUID id, String tenantId);
}
