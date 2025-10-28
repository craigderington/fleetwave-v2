package org.fleetwave.domain.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Workgroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkgroupRepository extends JpaRepository<Workgroup, UUID> {
  Optional<Workgroup> findByIdAndTenantId(UUID id, String tenantId);

  Optional<Workgroup> findByTenantIdAndName(String tenantId, String name);

  List<Workgroup> findAllByTenantId(String tenantId);

  Page<Workgroup> findAllByTenantId(String tenantId, Pageable pageable);
}
