package org.fleetwave.domain.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

  Optional<Assignment> findByTenantIdAndRadio_IdAndStatus(
      String tenantId, UUID radioId, Assignment.Status status);

  Optional<Assignment> findByIdAndTenantId(UUID id, String tenantId);

  List<Assignment> findAllByTenantIdAndRadio_IdAndStatus(
      String tenantId, UUID radioId, Assignment.Status status);

  List<Assignment> findAllByTenantIdAndAssigneePerson_IdAndStatus(
      String tenantId, UUID personId, Assignment.Status status);
}
