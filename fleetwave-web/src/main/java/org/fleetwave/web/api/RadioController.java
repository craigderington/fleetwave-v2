package org.fleetwave.web.api;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.*;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/radios")
public class RadioController {
  private final RadioRepository radios;

  public RadioController(RadioRepository r) {
    radios = r;
  }

  @GetMapping
  public Page<Radio> list(@PageableDefault(size = 20, sort = "serial") Pageable pageable) {
    return radios.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Radio> getById(@PathVariable UUID id) {
    Radio radio =
        radios.findById(id).orElseThrow(() -> new IllegalArgumentException("Radio not found"));
    return ResponseEntity.ok(radio);
  }

  @PostMapping
  public ResponseEntity<Radio> create(@RequestBody Radio radio) {
    if (radio.getId() == null) radio.setId(java.util.UUID.randomUUID());
    if (radio.getCreatedAt() == null) radio.setCreatedAt(OffsetDateTime.now());
    radio.setUpdatedAt(OffsetDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(radios.save(radio));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Radio> update(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id,
      @RequestBody Radio body) {
    Radio existing =
        radios
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Radio not found"));

    if (body.getSerial() != null) existing.setSerial(body.getSerial());
    if (body.getModel() != null) existing.setModel(body.getModel());
    if (body.getStatus() != null) existing.setStatus(body.getStatus());
    existing.setUpdatedAt(OffsetDateTime.now());

    return ResponseEntity.ok(radios.save(existing));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Radio> updateStatus(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id,
      @RequestParam Radio.Status status) {
    Radio existing =
        radios
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Radio not found"));

    existing.setStatus(status);
    existing.setUpdatedAt(OffsetDateTime.now());

    return ResponseEntity.ok(radios.save(existing));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id) {
    Radio existing =
        radios
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Radio not found"));
    radios.delete(existing);
    return ResponseEntity.noContent().build();
  }
}
