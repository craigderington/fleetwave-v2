package org.fleetwave.web.controllers;

import jakarta.validation.constraints.NotNull;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentApi {
  private final AssignmentService service;
  public AssignmentApi(AssignmentService service){ this.service = service; }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateAssignment req){
    Assignment a = service.create(req.radioId(), req.assigneeType(), req.assigneeId(), req.expectedEnd());
    return ResponseEntity.ok(Map.of("id", a.getId()));
  }

  @PostMapping("/{id}/return")
  public ResponseEntity<?> close(@PathVariable UUID id){
    Assignment a = service.returnAssignment(id);
    return ResponseEntity.ok(Map.of("id", a.getId(), "status", a.getStatus().name()));
  }

  public record CreateAssignment(@NotNull UUID radioId,
                                 @NotNull Assignment.AssigneeType assigneeType,
                                 @NotNull UUID assigneeId,
                                 OffsetDateTime expectedEnd){}
}
