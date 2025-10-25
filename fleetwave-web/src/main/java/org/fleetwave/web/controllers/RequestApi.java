package org.fleetwave.web.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.fleetwave.app.services.FulfillmentService;
import org.fleetwave.app.services.RequestService;
import org.fleetwave.domain.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestApi {
  private final RequestService service;
  private final FulfillmentService fulfill;

  public RequestApi(RequestService service, FulfillmentService fulfill) {
    this.service = service; this.fulfill = fulfill;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateRequest req){
    var r = service.create(req.requesterId(), req.workgroupId(), req.radioModelPref(), req.reason());
    return ResponseEntity.ok(Map.of("id", r.getId(), "status", r.getStatus()));
  }

  @PostMapping("/{id}/approve")
  public ResponseEntity<?> approve(@PathVariable UUID id, @RequestBody ApproveRequest req){
    var r = service.approve(id, req.approverId(), req.comment());
    return ResponseEntity.ok(Map.of("id", r.getId(), "status", r.getStatus()));
  }

  @PostMapping("/{id}/reject")
  public ResponseEntity<?> reject(@PathVariable UUID id, @RequestBody ApproveRequest req){
    var r = service.reject(id, req.approverId(), req.comment());
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
