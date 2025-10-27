package org.fleetwave.domain.repo;

import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByTenantIdAndEmail(String tenantId, String email);
    Optional<Person> findByIdAndEmail(UUID id, String email);
    Optional<Person> findByIdAndTenantId(UUID id, String tenantId);
}
