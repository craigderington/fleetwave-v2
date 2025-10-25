package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.Alert;
import org.fleetwave.domain.repo.AlertRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/alerts") @RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','SUPERVISOR')")
public class AlertApi {
  private final AlertRepository alerts;

  @PostMapping("/{id}/ack")
  public ResponseEntity<?> ack(@PathVariable UUID id){
    var a = alerts.findById(id).orElseThrow();
    a.setStatus(Alert.Status.ACK);
    alerts.save(a);
    return ResponseEntity.ok(Map.of("id", a.getId(), "status", a.getStatus()));
  }

  @PostMapping("/{id}/close")
  public ResponseEntity<?> close(@PathVariable UUID id){
    var a = alerts.findById(id).orElseThrow();
    a.setStatus(Alert.Status.CLOSED);
    alerts.save(a);
    return ResponseEntity.ok(Map.of("id", a.getId(), "status", a.getStatus()));
  }
}
