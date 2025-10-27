package org.fleetwave.domain.repo;

import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Workgroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkgroupRepository extends JpaRepository<Workgroup, UUID> {

  Optional<Workgroup> findByIdAndTenantId(UUID id, String tenantId);
}
