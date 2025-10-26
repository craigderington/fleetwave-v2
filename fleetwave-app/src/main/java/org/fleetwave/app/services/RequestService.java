package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.Request;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class RequestService {
  private final RequestRepository requests;

  @Transactional
  public Request create(UUID requesterId, UUID workgroupId, String modelPref, String reason){
    Request r = new Request();
    r.setId(UUID.randomUUID());
    r.setRequesterId(requesterId);
    var wg = new Workgroup(); wg.setId(workgroupId);
    r.setWorkgroup(wg);
    r.setRadioModelPref(modelPref);
    r.setReason(reason);
    r.setStatus(Request.Status.PENDING);
    return requests.save(r);
  }

  @Transactional
  public Request approve(UUID requestId){
    var r = requests.findById(requestId).orElseThrow();
    r.setStatus(Request.Status.APPROVED);
    return requests.save(r);
  }

  @Transactional
  public Request reject(UUID requestId){
    var r = requests.findById(requestId).orElseThrow();
    r.setStatus(Request.Status.REJECTED);
    return requests.save(r);
  }
}
