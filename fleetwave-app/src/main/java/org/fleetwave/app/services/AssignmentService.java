package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.fleetwave.domain.repo.RadioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignments;
  private final RadioRepository radios;

  @Transactional
  public Assignment create(UUID radioId, Assignment.AssigneeType assigneeType, UUID assigneeId, OffsetDateTime expectedEnd){
    var radio = radios.findById(radioId).orElseThrow();
    assignments.findByRadio_IdAndStatus(radioId, Assignment.Status.ACTIVE).ifPresent(a -> { throw new IllegalStateException("Radio already assigned"); });
    Assignment a = new Assignment();
    a.setId(UUID.randomUUID());
    a.setRadio(radio);
    a.setAssigneeType(assigneeType);
    a.setAssigneeId(assigneeId);
    a.setExpectedEnd(expectedEnd);
    a.setStatus(Assignment.Status.ACTIVE);
    return assignments.save(a);
  }

  @Transactional
  public Assignment returnAssignment(UUID id){
    var a = assignments.findById(id).orElseThrow();
    a.setEndAt(OffsetDateTime.now());
    a.setStatus(Assignment.Status.RETURNED);
    return assignments.save(a);
  }
}
