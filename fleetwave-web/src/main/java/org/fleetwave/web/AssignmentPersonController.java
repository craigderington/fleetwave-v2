package org.fleetwave.web;

import jakarta.validation.Valid;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.web.dto.AssignmentDtos.AssignToPersonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping(path = "/api/assignments/person", produces = "application/json")
public class AssignmentPersonController {

  private final AssignmentService assignmentService;

  public AssignmentPersonController(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
  }

  @PostMapping("/assign")
  public ResponseEntity<Void> assignToPerson(
      @RequestHeader("X-Tenant") String tenantId,
      @Valid @RequestBody AssignToPersonRequest body) {

    OffsetDateTime expected = body.getExpectedEnd();
    assignmentService.assignToPerson(
        tenantId, body.getRadioId(), body.getPersonId(), expected);
    return ResponseEntity.ok().build();
  }
}
