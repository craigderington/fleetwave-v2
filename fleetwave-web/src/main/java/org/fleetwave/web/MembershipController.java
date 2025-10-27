package org.fleetwave.web;

import org.fleetwave.domain.Person;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.domain.repo.WorkgroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/memberships", produces = "application/json")
public class MembershipController {

  private final PersonRepository personRepo;
  private final WorkgroupRepository workgroupRepo;

  public MembershipController(PersonRepository personRepo, WorkgroupRepository workgroupRepo) {
    this.personRepo = personRepo;
    this.workgroupRepo = workgroupRepo;
  }

  @PostMapping("/{workgroupId}/add/{personId}")
  public ResponseEntity<Void> addMember(
      @RequestHeader("X-Tenant") String tenantId,
      @PathVariable UUID workgroupId,
      @PathVariable UUID personId) {

    Workgroup wg = workgroupRepo.findByIdAndTenantId(workgroupId, tenantId)
        .orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));

    Person p = personRepo.findByIdAndTenantId(personId, tenantId)
        .orElseThrow(() -> new IllegalArgumentException("Person not found"));

    wg.getMembers().add(p);
    workgroupRepo.save(wg);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{workgroupId}/remove/{personId}")
  public ResponseEntity<Void> removeMember(
      @RequestHeader("X-Tenant") String tenantId,
      @PathVariable UUID workgroupId,
      @PathVariable UUID personId) {

    Workgroup wg = workgroupRepo.findByIdAndTenantId(workgroupId, tenantId)
        .orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));
    wg.getMembers().removeIf(x -> x.getId().equals(personId));
    workgroupRepo.save(wg);
    return ResponseEntity.ok().build();
  }
}
