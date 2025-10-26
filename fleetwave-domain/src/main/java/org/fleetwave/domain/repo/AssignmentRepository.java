package org.fleetwave.domain.repo;
import org.fleetwave.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
  Optional<Assignment> findByRadio_IdAndStatus(UUID radioId, Assignment.Status status);
  @Query("select a from Assignment a where a.status=org.fleetwave.domain.Assignment$Status.ACTIVE and a.expectedEnd < :now")
  List<Assignment> findOverdue(@Param("now") OffsetDateTime now);
}
