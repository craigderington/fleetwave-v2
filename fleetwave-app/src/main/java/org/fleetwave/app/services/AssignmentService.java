package org.fleetwave.app.services;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.Radio;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.domain.repo.RadioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignmentService {

  private final AssignmentRepository assignmentRepo;
  private final RadioRepository radioRepo;
  private final PersonRepository personRepo;

  public AssignmentService(
      AssignmentRepository assignmentRepo,
      RadioRepository radioRepo,
      PersonRepository personRepo) {
    this.assignmentRepo = assignmentRepo;
    this.radioRepo = radioRepo;
    this.personRepo = personRepo;
  }

  /** Assign radio -> Person (enforce one-active-assignment-per-radio). */
  @Transactional
  public Assignment assignToPerson(
      String tenantId, UUID radioId, UUID personId, OffsetDateTime expectedEnd) {

    if (tenantId == null || radioId == null || personId == null) {
      throw new IllegalArgumentException("tenantId, radioId and personId are required");
    }

    Radio radio =
        radioRepo
            .findByIdAndTenantId(radioId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Radio not found"));

    Person person =
        personRepo
            .findByIdAndTenantId(personId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found"));

    assignmentRepo
        .findByTenantIdAndRadio_IdAndStatus(tenantId, radio.getId(), Assignment.Status.ACTIVE)
        .ifPresent(a -> {
          throw new IllegalStateException("Radio already has an active assignment");
        });

    OffsetDateTime now = OffsetDateTime.now();
    OffsetDateTime expected = (expectedEnd != null) ? expectedEnd : now.plusDays(7);

    Assignment a = new Assignment();
    a.setId(UUID.randomUUID());
    a.setTenantId(tenantId);
    a.setRadio(radio);
    a.setAssigneePerson(person);
    a.setAssigneeWorkgroup(null);
    a.setStartAt(now);
    a.setExpectedEnd(expected);
    a.setStatus(Assignment.Status.ACTIVE);
    a.setCreatedAt(now);
    a.setUpdatedAt(now);

    return assignmentRepo.save(a);
  }

  /** Return an active assignment (idempotent if already returned). */
  @Transactional
  public Assignment returnAssignment(
      String tenantId, UUID assignmentId, OffsetDateTime returnedAt) {

    if (tenantId == null || assignmentId == null) {
      throw new IllegalArgumentException("tenantId and assignmentId are required");
    }

    Assignment a =
        assignmentRepo
            .findByIdAndTenantId(assignmentId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

    if (a.getStatus() == Assignment.Status.RETURNED) {
      // idempotent behavior: update endAt if missing, keep returned
      if (a.getEndAt() == null && returnedAt != null) {
        a.setEndAt(returnedAt);
        a.setUpdatedAt(OffsetDateTime.now());
        return assignmentRepo.save(a);
      }
      return a;
    }

    OffsetDateTime now = OffsetDateTime.now();
    a.setStatus(Assignment.Status.RETURNED);
    a.setEndAt(returnedAt != null ? returnedAt : now);
    a.setUpdatedAt(now);

    return assignmentRepo.save(a);
  }
}
