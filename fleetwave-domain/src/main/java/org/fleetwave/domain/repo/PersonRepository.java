package org.fleetwave.domain.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {
  Optional<Person> findByTenantIdAndEmail(String tenantId, String email);

  Optional<Person> findByIdAndEmail(UUID id, String email);

  Optional<Person> findByIdAndTenantId(UUID id, String tenantId);

  List<Person> findAllByTenantId(String tenantId);

  Page<Person> findAllByTenantId(String tenantId, Pageable pageable);
}
