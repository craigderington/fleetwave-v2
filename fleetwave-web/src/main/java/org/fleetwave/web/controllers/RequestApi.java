package org.fleetwave.web.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.fleetwave.app.services.FulfillmentService;
import org.fleetwave.app.services.RequestService;
import org.fleetwave.domain.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','MANAGER')")
public class RequestApi {
  private final RequestService service;
  private final FulfillmentService fulfill;

  public RequestApi(RequestService service, FulfillmentService fulfill) {
    this.service = service; this.fulfill = fulfill;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateRequest req, org.springframework.security.core.Authentication auth){
    var requester = req.requesterId() != null ? req.requesterId() : users.findByEmail(auth.getName()).orElseThrow().getId();
    var r = service.create(requester, req.workgroupId(), req.radioModelPref(), req.reason());
    return ResponseEntity.ok(Map.of("id", r.getId(), "status", r.getStatus()));
  }

  @PostMapping("/{id}/approve")
  public ResponseEntity<?> approve(@PathVariable UUID id, @RequestBody ApproveRequest req, org.springframework.security.core.Authentication auth){
    var approver = req.approverId() != null ? req.approverId() : users.findByEmail(auth.getName()).orElseThrow().getId();
    var r = service.approve(id, approver, req.comment());
    return ResponseEntity.ok(Map.of("id", r.getId(), "status", r.getStatus()));
  }

  @PostMapping("/{id}/reject")
  public ResponseEntity<?> reject(@PathVariable UUID id, @RequestBody ApproveRequest req, org.springframework.security.core.Authentication auth){
    var approver = req.approverId() != null ? req.approverId() : users.findByEmail(auth.getName()).orElseThrow().getId();
    var r = service.reject(id, approver, req.comment());
    return ResponseEntity.ok(Map.of("id", r.getId(), "status", r.getStatus()));
  }

  @PostMapping("/{id}/fulfill")
  public ResponseEntity<?> fulfill(@PathVariable UUID id){
    var a = fulfill.fulfill(id);
    return ResponseEntity.ok(Map.of("assignmentId", a.getId(), "status", a.getStatus()));
  }

  public record CreateRequest(@NotNull UUID requesterId, @NotNull UUID workgroupId,
                              String radioModelPref, @NotBlank String reason){}
  public record ApproveRequest(@NotNull UUID approverId, String comment){}
}

  @GetMapping
  public ResponseEntity<?> list(@RequestParam(required=false) String status,
                                @RequestParam(required=false, defaultValue="false") boolean mine,
                                org.springframework.security.core.Authentication auth){
    if (mine){
      var u = users.findByEmail(auth.getName()).orElseThrow();
      return ResponseEntity.ok(repo.findByRequesterId(u.getId()));
    }
    var all = repo.findAll();
    if (status != null) {
      var s = org.fleetwave.domain.Request.Status.valueOf(status);
      all = all.stream().filter(r -> r.getStatus() == s).toList();
    }
    return ResponseEntity.ok(all);
  }
