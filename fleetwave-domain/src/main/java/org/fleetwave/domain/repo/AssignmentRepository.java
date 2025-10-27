package org.fleetwave.domain.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.fleetwave.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByTenantId(String tenantId);

    List<Assignment> findByTenantIdAndStatusAndDueAtBefore(String tenantId, Assignment.Status status, OffsetDateTime cutoff);

    @Query("""
        select a from Assignment a
        where a.status = 'ACTIVE' and a.dueAt is not null and a.dueAt < :now
    """)
    List<Assignment> findOverdue(@Param("now") OffsetDateTime now);
}
