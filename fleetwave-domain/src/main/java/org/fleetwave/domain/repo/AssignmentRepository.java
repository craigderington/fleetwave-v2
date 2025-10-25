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

  @Query("select a from Assignment a where a.status = org.fleetwave.domain.Assignment$Status.ACTIVE " +
         "and a.endAt is null and a.expectedEnd < :now")
  List<Assignment> findOverdue(@Param("now") OffsetDateTime now);

  @Query("select a from Assignment a where a.radio.id = :radioId and a.status = " +
         "org.fleetwave.domain.Assignment$Status.ACTIVE and a.endAt is null")
  Optional<Assignment> findActiveForRadio(@Param("radioId") UUID radioId);
}


  @Query("select a from Assignment a where a.status = org.fleetwave.domain.Assignment$Status.ACTIVE " +
         "and a.endAt is null and a.assigneeType = org.fleetwave.domain.Assignment$AssigneeType.USER " +
         "and a.assigneeId = :userId")
  List<Assignment> findActiveByUser(@Param("userId") UUID userId);
