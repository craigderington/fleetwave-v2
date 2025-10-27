package org.fleetwave.domain.repo;

import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Radio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadioRepository extends JpaRepository<Radio, UUID> {

  Optional<Radio> findByIdAndTenantId(UUID id, String tenantId);
}
