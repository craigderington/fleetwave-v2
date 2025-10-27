package org.fleetwave.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.fleetwave.web.dto.AssignmentDtos.AssignToPersonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Assignments")
public class AssignmentPersonController {

  private final AssignmentService assignmentService;

  public AssignmentPersonController(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
  }

  @PostMapping("/person")
  @Transactional
  public ResponseEntity<Assignment> assignToPerson(
      @RequestHeader("X-Tenant") String tenantId, @RequestBody AssignToPersonRequest req) {

    if (req.radioId == null || req.personId == null) {
      return ResponseEntity.badRequest().build();
    }

    OffsetDateTime expected =
        (req.expectedEnd == null) ? OffsetDateTime.now().plusDays(7) : req.expectedEnd;

    Assignment a =
        assignmentService.assignToPerson(tenantId, req.radioId, req.personId, expected);

    return ResponseEntity.ok(a);
  }
}
