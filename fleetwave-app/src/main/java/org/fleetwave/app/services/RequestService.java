package org.fleetwave.app.services;

import org.fleetwave.domain.Request;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;

@Service
public class RequestService {
  private final RequestRepository requests;

  @Autowired
  public RequestService(RequestRepository requests){
    this.requests = requests;
  }

  @Transactional
  public Request create(UUID requesterId, UUID workgroupId, String modelPref, String reason){
    Request r = new Request();
    r.setId(UUID.randomUUID());
    r.setRequesterId(requesterId);
    Workgroup wg = new Workgroup(); wg.setId(workgroupId);
    r.setWorkgroup(wg);
    r.setRadioModelPref(modelPref);
    r.setReason(reason);
    r.setStatus(Request.Status.PENDING);
    return requests.save(r);
  }

  @Transactional
  public Request approve(UUID requestId){
    Request r = requests.findById(requestId).orElseThrow();
    r.setStatus(Request.Status.APPROVED);
    return requests.save(r);
  }

  @Transactional
  public Request reject(UUID requestId){
    Request r = requests.findById(requestId).orElseThrow();
    r.setStatus(Request.Status.REJECTED);
    return requests.save(r);
  }
}
