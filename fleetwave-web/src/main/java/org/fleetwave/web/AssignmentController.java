package org.fleetwave.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.fleetwave.web.dto.AssignmentDtos.ReturnRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Assignments")
public class AssignmentController {

  private final AssignmentRepository assignmentRepo;
  private final AssignmentService assignmentService;

  public AssignmentController(
      AssignmentRepository assignmentRepo, AssignmentService assignmentService) {
    this.assignmentRepo = assignmentRepo;
    this.assignmentService = assignmentService;
  }

  @GetMapping("/active-by-radio/{radioId}")
  public List<Assignment> activeByRadio(
      @RequestHeader("X-Tenant") String tenantId, @PathVariable("radioId") UUID radioId) {
    return assignmentRepo.findAllByTenantIdAndRadio_IdAndStatus(
        tenantId, radioId, Assignment.Status.ACTIVE);
  }

  @GetMapping("/active-by-person/{personId}")
  public List<Assignment> activeByPerson(
      @RequestHeader("X-Tenant") String tenantId, @PathVariable("personId") UUID personId) {
    return assignmentRepo.findAllByTenantIdAndAssigneePerson_IdAndStatus(
        tenantId, personId, Assignment.Status.ACTIVE);
  }

  @PostMapping("/{assignmentId}/return")
  @Transactional
  public ResponseEntity<Assignment> returnAssignment(
      @RequestHeader("X-Tenant") String tenantId,
      @PathVariable("assignmentId") UUID assignmentId,
      @RequestParam(value = "returnedAt", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime returnedAt) {

    Assignment a = assignmentService.returnAssignment(tenantId, assignmentId, returnedAt);
    return ResponseEntity.ok(a);
  }

  // Alternative body-based endpoint (useful for clients that prefer JSON)
  @PostMapping("/return")
  @Transactional
  public ResponseEntity<Assignment> returnAssignmentBody(
      @RequestHeader("X-Tenant") String tenantId,
      @RequestBody ReturnRequest req) {

    if (req == null || req.assignmentId == null) {
      return ResponseEntity.badRequest().build();
    }
    OffsetDateTime ts = null;
    if (req.returnedAt != null && !req.returnedAt.isBlank()) {
      ts = OffsetDateTime.parse(req.returnedAt);
    }
    Assignment a = assignmentService.returnAssignment(tenantId, req.assignmentId, ts);
    return ResponseEntity.ok(a);
  }
}
