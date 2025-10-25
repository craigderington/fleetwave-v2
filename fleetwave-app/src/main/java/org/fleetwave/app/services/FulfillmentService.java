package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.app.events.DomainEvents;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class FulfillmentService {
  private final RequestRepository requests;
  private final RadioRepository radios;
  private final AssignmentRepository assignments;
  private final DomainEvents events;

  @Transactional
  public Assignment fulfill(UUID requestId){
    var req = requests.findById(requestId).orElseThrow();
    if (req.getStatus() != Request.Status.APPROVED) throw new IllegalStateException("Request not approved");
    // naive selection: first available ACTIVE radio
    var radio = radios.findFirstByStatusOrderByCreatedAtAsc(Radio.Status.ACTIVE).orElseThrow();
    assignments.findActiveForRadio(radio.getId()).ifPresent(a -> { throw new IllegalStateException("Selected radio already assigned"); });
    var a = Assignment.builder()
      .id(UUID.randomUUID())
      .radio(radio)
      .assigneeType(Assignment.AssigneeType.WORKGROUP)
      .assigneeId(req.getWorkgroup().getId())
      .expectedEnd(req.getNeededUntil())
      .status(Assignment.Status.ACTIVE)
      .build();
    assignments.save(a);
    req.setStatus(Request.Status.FULFILLED);
    events.publish(a);
    return a;
  }
}
