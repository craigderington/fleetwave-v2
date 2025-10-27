package org.fleetwave.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.fleetwave.domain.Membership;
import org.fleetwave.domain.Membership.Role;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.MembershipRepository;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.domain.repo.WorkgroupRepository;
import org.fleetwave.web.dto.MembershipDtos.AddMembershipRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
@Tag(name = "Memberships")
public class MembershipController {

  private final MembershipRepository membershipRepo;
  private final PersonRepository personRepo;
  private final WorkgroupRepository workgroupRepo;

  public MembershipController(
      MembershipRepository membershipRepo,
      PersonRepository personRepo,
      WorkgroupRepository workgroupRepo) {
    this.membershipRepo = membershipRepo;
    this.personRepo = personRepo;
    this.workgroupRepo = workgroupRepo;
  }

  @GetMapping("/by-workgroup/{workgroupId}")
  public List<Membership> listByWorkgroup(
      @RequestHeader("X-Tenant") String tenantId, @PathVariable("workgroupId") UUID workgroupId) {
    return membershipRepo.findAllByTenantIdAndWorkgroup_Id(tenantId, workgroupId);
  }

  @GetMapping("/by-person/{personId}")
  public List<Membership> listByPerson(
      @RequestHeader("X-Tenant") String tenantId, @PathVariable("personId") UUID personId) {
    return membershipRepo.findAllByTenantIdAndPerson_Id(tenantId, personId);
  }

  @PostMapping
  @Transactional
  public ResponseEntity<Membership> add(
      @RequestHeader("X-Tenant") String tenantId, @RequestBody AddMembershipRequest req) {

    Person p =
        personRepo
            .findByIdAndTenantId(req.personId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found"));

    // Requires WorkgroupRepository to have: Optional<Workgroup> findByIdAndTenantId(UUID, String)
    Workgroup g =
        workgroupRepo
            .findByIdAndTenantId(req.workgroupId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));

    Role role = (req.role == null) ? Role.MEMBER : Role.valueOf(req.role);

    Membership m =
        membershipRepo
            .findByTenantIdAndPerson_IdAndWorkgroup_Id(tenantId, p.getId(), g.getId())
            .orElse(Membership.createNew(tenantId, p, g, role));

    m.setRole(role);
    return ResponseEntity.ok(membershipRepo.save(m));
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity<Void> remove(
      @RequestHeader("X-Tenant") String tenantId,
      @RequestParam("personId") UUID personId,
      @RequestParam("workgroupId") UUID workgroupId) {

    return membershipRepo
        .findByTenantIdAndPerson_IdAndWorkgroup_Id(tenantId, personId, workgroupId)
        .map(
            m -> {
              membershipRepo.delete(m);
              return ResponseEntity.<Void>noContent().build();
            })
        .orElseGet(() -> ResponseEntity.<Void>notFound().build());
  }
}
