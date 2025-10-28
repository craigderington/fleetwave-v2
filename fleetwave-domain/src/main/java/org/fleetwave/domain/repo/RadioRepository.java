package org.fleetwave.domain.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Radio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadioRepository extends JpaRepository<Radio, UUID> {
  Optional<Radio> findByIdAndTenantId(UUID id, String tenantId);

  Optional<Radio> findByTenantIdAndSerial(String tenantId, String serial);

  List<Radio> findAllByTenantId(String tenantId);

  Page<Radio> findAllByTenantId(String tenantId, Pageable pageable);

  Page<Radio> findByTenantIdAndStatus(
      String tenantId, Radio.Status status, Pageable pageable);
}
