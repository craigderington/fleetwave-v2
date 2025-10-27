package org.fleetwave.web.api;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.app.services.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/assignments", produces = "application/json")
public class AssignmentController {

  private final AssignmentService assignmentService;

  public AssignmentController(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
  }

  @PostMapping("/{assignmentId}/return")
  public ResponseEntity<Void> returnAssignment(
      @RequestHeader("X-Tenant") String tenantId,
      @PathVariable UUID assignmentId) {

    assignmentService.returnAssignment(tenantId, assignmentId, OffsetDateTime.now());
    return ResponseEntity.ok().build();
  }
}
