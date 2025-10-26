package org.fleetwave.app.services;
import org.fleetwave.domain.*; import org.fleetwave.domain.repo.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.beans.factory.annotation.Autowired; import java.time.OffsetDateTime; import java.util.UUID;
@Service public class AssignmentService {
  private final AssignmentRepository assignments; private final RadioRepository radios;
  @Autowired public AssignmentService(AssignmentRepository a, RadioRepository r){ assignments=a; radios=r; }
  @Transactional public Assignment create(UUID radioId, Assignment.AssigneeType t, UUID assigneeId, OffsetDateTime expectedEnd){
    Radio radio = radios.findById(radioId).orElseThrow();
    assignments.findByRadio_IdAndStatus(radioId, Assignment.Status.ACTIVE).ifPresent(x->{ throw new IllegalStateException("Radio already assigned"); });
    Assignment a = new Assignment(); a.setId(UUID.randomUUID()); a.setRadio(radio); a.setAssigneeType(t); a.setAssigneeId(assigneeId); a.setExpectedEnd(expectedEnd); a.setStatus(Assignment.Status.ACTIVE);
    return assignments.save(a);
  }
  @Transactional public Assignment returnAssignment(UUID id){ Assignment a=assignments.findById(id).orElseThrow(); a.setEndAt(OffsetDateTime.now()); a.setStatus(Assignment.Status.RETURNED); return assignments.save(a); }
}