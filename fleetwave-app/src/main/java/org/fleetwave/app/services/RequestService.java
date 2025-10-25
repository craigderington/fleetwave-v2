package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class RequestService {
  private final RequestRepository requests;
  private final ApprovalRepository approvals;

  @Transactional
  public Request create(UUID requesterId, UUID workgroupId, String modelPref, String reason){
    var r = Request.builder()
      .id(UUID.randomUUID())
      .requesterId(requesterId)
      .workgroup(Workgroup.builder().id(workgroupId).build())
      .radioModelPref(modelPref)
      .reason(reason)
      .build();
    return requests.save(r);
  }

  @Transactional
  public Request approve(UUID requestId, UUID approverId, String comment){
    var r = requests.findById(requestId).orElseThrow();
    r.setStatus(Request.Status.APPROVED);
    approvals.save(Approval.builder()
      .id(UUID.randomUUID())
      .request(r).approverId(approverId).decision(Approval.Decision.APPROVE).comment(comment).build());
    return r;
  }

  @Transactional
  public Request reject(UUID requestId, UUID approverId, String comment){
    var r = requests.findById(requestId).orElseThrow();
    r.setStatus(Request.Status.REJECTED);
    approvals.save(Approval.builder()
      .id(UUID.randomUUID())
      .request(r).approverId(approverId).decision(Approval.Decision.REJECT).comment(comment).build());
    return r;
  }
}
