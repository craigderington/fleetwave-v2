package org.fleetwave.web.api;

import java.util.*;
import org.fleetwave.domain.Alert;
import org.fleetwave.domain.repo.AlertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
  private final AlertRepository repo;

  public AlertController(AlertRepository r) {
    repo = r;
  }

  @GetMapping
  public Page<Alert> list(@PageableDefault(size = 20, sort = "lastSeen") Pageable pageable) {
    return repo.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Alert> getById(@PathVariable UUID id) {
    Alert alert =
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found"));
    return ResponseEntity.ok(alert);
  }

  @PostMapping("/{id}/acknowledge")
  public ResponseEntity<Alert> acknowledge(@PathVariable UUID id) {
    Alert alert =
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found"));

    if (alert.getStatus() == Alert.Status.OPEN) {
      alert.setStatus(Alert.Status.ACK);
      return ResponseEntity.ok(repo.save(alert));
    }
    return ResponseEntity.ok(alert);
  }

  @PostMapping("/{id}/close")
  public ResponseEntity<Alert> close(@PathVariable UUID id) {
    Alert alert =
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found"));

    alert.setStatus(Alert.Status.CLOSED);
    return ResponseEntity.ok(repo.save(alert));
  }
}