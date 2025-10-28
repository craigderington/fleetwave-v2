package org.fleetwave.web.api;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/assignments", produces = "application/json")
public class AssignmentController {

  private final AssignmentService assignmentService;
  private final AssignmentRepository assignmentRepo;

  public AssignmentController(
      AssignmentService assignmentService, AssignmentRepository assignmentRepo) {
    this.assignmentService = assignmentService;
    this.assignmentRepo = assignmentRepo;
  }

  @GetMapping
  public ResponseEntity<Page<Assignment>> list(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @RequestParam(required = false) Assignment.Status status,
      @PageableDefault(size = 20, sort = "startAt") Pageable pageable) {
    if (status != null) {
      return ResponseEntity.ok(assignmentRepo.findAllByTenantIdAndStatus(tenantId, status, pageable));
    }
    return ResponseEntity.ok(assignmentRepo.findAllByTenantId(tenantId, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Assignment> getById(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id) {
    Assignment assignment =
        assignmentRepo
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
    return ResponseEntity.ok(assignment);
  }

  @GetMapping("/active")
  public ResponseEntity<Page<Assignment>> listActive(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PageableDefault(size = 20, sort = "startAt") Pageable pageable) {
    return ResponseEntity.ok(
        assignmentRepo.findAllByTenantIdAndStatus(tenantId, Assignment.Status.ASSIGNED, pageable));
  }

  @GetMapping("/by-radio/{radioId}")
  public ResponseEntity<Page<Assignment>> getByRadio(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID radioId,
      @PageableDefault(size = 20, sort = "startAt") Pageable pageable) {
    return ResponseEntity.ok(
        assignmentRepo.findAllByTenantIdAndRadio_Id(tenantId, radioId, pageable));
  }

  @GetMapping("/by-person/{personId}")
  public ResponseEntity<Page<Assignment>> getByPerson(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID personId,
      @PageableDefault(size = 20, sort = "startAt") Pageable pageable) {
    return ResponseEntity.ok(
        assignmentRepo.findAllByTenantIdAndAssigneePerson_Id(tenantId, personId, pageable));
  }

  @GetMapping("/by-workgroup/{workgroupId}")
  public ResponseEntity<Page<Assignment>> getByWorkgroup(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID workgroupId,
      @PageableDefault(size = 20, sort = "startAt") Pageable pageable) {
    return ResponseEntity.ok(
        assignmentRepo.findAllByTenantIdAndAssigneeWorkgroup_Id(tenantId, workgroupId, pageable));
  }

  @GetMapping("/overdue")
  public ResponseEntity<List<Assignment>> getOverdue() {
    return ResponseEntity.ok(assignmentRepo.findOverdue(OffsetDateTime.now()));
  }

  @PostMapping("/{assignmentId}/return")
  public ResponseEntity<Assignment> returnAssignment(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID assignmentId) {
    Assignment returned =
        assignmentService.returnAssignment(tenantId, assignmentId, OffsetDateTime.now());
    return ResponseEntity.ok(returned);
  }
}
