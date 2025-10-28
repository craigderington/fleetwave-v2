package org.fleetwave.domain.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

  // Needed by AssignmentService (line 50)
  Optional<Assignment> findByTenantIdAndRadio_IdAndStatus(
      String tenantId, UUID radioId, Assignment.Status status);

  // Needed by AssignmentService (line 84)
  Optional<Assignment> findByIdAndTenantId(UUID id, String tenantId);

  // Tenant-scoped queries
  Page<Assignment> findAllByTenantId(String tenantId, Pageable pageable);

  Page<Assignment> findAllByTenantIdAndStatus(
      String tenantId, Assignment.Status status, Pageable pageable);

  // Radio history
  List<Assignment> findAllByTenantIdAndRadio_Id(String tenantId, UUID radioId);

  Page<Assignment> findAllByTenantIdAndRadio_Id(
      String tenantId, UUID radioId, Pageable pageable);

  // Person history
  List<Assignment> findAllByTenantIdAndAssigneePerson_Id(String tenantId, UUID personId);

  Page<Assignment> findAllByTenantIdAndAssigneePerson_Id(
      String tenantId, UUID personId, Pageable pageable);

  // Workgroup history
  List<Assignment> findAllByTenantIdAndAssigneeWorkgroup_Id(String tenantId, UUID workgroupId);

  Page<Assignment> findAllByTenantIdAndAssigneeWorkgroup_Id(
      String tenantId, UUID workgroupId, Pageable pageable);

  // Used by OverdueScanner previously; if your scanner calls findOverdue(now)
  @Query(
      """
           select a
             from Assignment a
            where a.expectedEnd is not null
              and a.expectedEnd < :now
              and a.status = org.fleetwave.domain.Assignment.Status.ASSIGNED
           """)
  List<Assignment> findOverdue(@Param("now") OffsetDateTime now);
}

