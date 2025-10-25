package org.fleetwave.web.controllers;

import jakarta.validation.constraints.NotNull;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.fleetwave.domain.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentApi {
  private final AssignmentService service;
  private final AssignmentRepository repo;
  private final UserRepository users;

  public AssignmentApi(AssignmentService service, AssignmentRepository repo, UserRepository users){
    this.service = service; this.repo = repo; this.users = users;
  }

  @GetMapping
  public ResponseEntity<?> list(@RequestParam(required=false) String status,
                                @RequestParam(required=false, defaultValue = "false") boolean my,
                                Authentication auth){
    if (my){
      var u = users.findByEmail(auth.getName()).orElseThrow();
      return ResponseEntity.ok(repo.findActiveByUser(u.getId()));
    }
    return ResponseEntity.ok(List.of());
  }

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
