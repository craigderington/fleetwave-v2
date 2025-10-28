package org.fleetwave.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.fleetwave.web.dto.AssignmentDtos.AssignToPersonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/assignments/person", produces = APPLICATION_JSON_VALUE)
public class AssignmentPersonController {

  private final AssignmentService assignmentService;

  public AssignmentPersonController(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<Assignment> assignToPerson(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required") String tenantId,
      @Valid @RequestBody AssignToPersonRequest body) {
    Assignment a =
        assignmentService.assignToPerson(
            tenantId,
            body.getRadioId(),
            body.getPersonId(),
            body.getDueAt() != null ? body.getDueAt() : OffsetDateTime.now().plusDays(7));
    return ResponseEntity.ok(a);
  }

  @PostMapping("/{id}/return")
  public ResponseEntity<Assignment> returnFromPerson(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required") String tenantId,
      @PathVariable("id") UUID assignmentId) {
    Assignment a =
        assignmentService.returnAssignment(tenantId, assignmentId, OffsetDateTime.now());
    return ResponseEntity.ok(a);
  }
}
