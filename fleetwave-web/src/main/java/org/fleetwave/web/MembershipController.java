package org.fleetwave.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.fleetwave.domain.Membership;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.MembershipRepository;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.domain.repo.WorkgroupRepository;
import org.fleetwave.web.dto.MembershipDtos.AddMembershipRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/memberships", produces = APPLICATION_JSON_VALUE)
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

  @GetMapping
  public ResponseEntity<List<Membership>> list() {
    return ResponseEntity.ok(membershipRepo.findAll());
  }

  @GetMapping("/by-workgroup/{workgroupId}")
  public ResponseEntity<List<Membership>> getByWorkgroup(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID workgroupId) {
    return ResponseEntity.ok(
        membershipRepo.findAllByTenantIdAndWorkgroup_Id(tenantId, workgroupId));
  }

  @GetMapping("/by-person/{personId}")
  public ResponseEntity<List<Membership>> getByPerson(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID personId) {
    return ResponseEntity.ok(membershipRepo.findAllByTenantIdAndPerson_Id(tenantId, personId));
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<Membership> addMembership(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @Valid @RequestBody AddMembershipRequest body) {

    Person person =
        personRepo
            .findByIdAndTenantId(body.getPersonId(), tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found"));

    Workgroup workgroup =
        workgroupRepo
            .findByIdAndTenantId(body.getWorkgroupId(), tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));

    // Check if membership already exists
    java.util.Optional<Membership> existing =
        membershipRepo.findByTenantIdAndPerson_IdAndWorkgroup_Id(
            tenantId, body.getPersonId(), body.getWorkgroupId());

    if (existing.isPresent()) {
      // Update existing membership role
      Membership m = existing.get();
      m.setRole(Membership.Role.valueOf(body.getRole()));
      return ResponseEntity.ok(membershipRepo.save(m));
    }

    // Create new membership using factory method
    Membership membership =
        Membership.createNew(tenantId, person, workgroup, Membership.Role.valueOf(body.getRole()));

    return ResponseEntity.status(HttpStatus.CREATED).body(membershipRepo.save(membership));
  }

  @DeleteMapping
  public ResponseEntity<Void> removeMembership(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @RequestParam UUID personId,
      @RequestParam UUID workgroupId) {

    Membership membership =
        membershipRepo
            .findByTenantIdAndPerson_IdAndWorkgroup_Id(tenantId, personId, workgroupId)
            .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

    membershipRepo.delete(membership);
    return ResponseEntity.noContent().build();
  }
}
