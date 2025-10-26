package org.fleetwave.app.services;
import org.fleetwave.domain.*; import org.fleetwave.domain.repo.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.beans.factory.annotation.Autowired; import java.util.UUID;
@Service public class RequestService {
  private final RequestRepository requests;
  @Autowired public RequestService(RequestRepository r){ this.requests=r; }
  @Transactional public Request create(UUID requesterId, UUID workgroupId, String modelPref, String reason){
    Request x = new Request(); x.setId(UUID.randomUUID()); x.setRequesterId(requesterId); Workgroup wg=new Workgroup(); wg.setId(workgroupId); x.setWorkgroup(wg); x.setRadioModelPref(modelPref); x.setReason(reason); x.setStatus(Request.Status.PENDING); return requests.save(x);
  }
  @Transactional public Request approve(UUID id){ Request r=requests.findById(id).orElseThrow(); r.setStatus(Request.Status.APPROVED); return requests.save(r); }
  @Transactional public Request reject(UUID id){ Request r=requests.findById(id).orElseThrow(); r.setStatus(Request.Status.REJECTED); return requests.save(r); }
}