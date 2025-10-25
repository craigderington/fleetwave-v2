package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.app.events.DomainEvents;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.fleetwave.domain.repo.RadioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignments;
  private final RadioRepository radios;
  private final DomainEvents events;

  @Transactional
  public Assignment create(UUID radioId, Assignment.AssigneeType type, UUID assigneeId, OffsetDateTime expectedEnd){
    assignments.findActiveForRadio(radioId).ifPresent(a -> { throw new IllegalStateException("Radio already assigned"); });
    var radio = radios.findById(radioId).orElseThrow();
    var a = Assignment.builder()
        .id(UUID.randomUUID())
        .radio(radio)
        .assigneeType(type)
        .assigneeId(assigneeId)
        .expectedEnd(expectedEnd)
        .status(Assignment.Status.ACTIVE)
        .build();
    var saved = assignments.save(a);
    events.publish(saved);
    return saved;
  }

  @Transactional
  public Assignment returnAssignment(UUID assignmentId){
    var a = assignments.findById(assignmentId).orElseThrow();
    a.setEndAt(OffsetDateTime.now());
    a.setStatus(Assignment.Status.RETURNED);
    return a;
  }
}
