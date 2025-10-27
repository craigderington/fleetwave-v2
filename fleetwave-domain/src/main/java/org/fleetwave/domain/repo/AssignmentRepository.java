package org.fleetwave.domain.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

  Optional<Assignment> findByIdAndTenantId(UUID id, String tenantId);

  Optional<Assignment> findByTenantIdAndRadio_IdAndStatus(
      String tenantId, UUID radioId, Assignment.Status status);

  List<Assignment> findAllByTenantIdAndRadio_IdAndStatus(
      String tenantId, UUID radioId, Assignment.Status status);

  List<Assignment> findAllByTenantIdAndAssigneePerson_IdAndStatus(
      String tenantId, UUID personId, Assignment.Status status);

  /**
   * Used by OverdueScanner. Returns all ACTIVE assignments whose expectedEnd is before :now.
   */
  @Query("""
         select a
           from Assignment a
          where a.status = org.fleetwave.domain.Assignment.Status.ACTIVE
            and a.expectedEnd < :now
         """)
  List<Assignment> findOverdue(@Param("now") OffsetDateTime now);
}
