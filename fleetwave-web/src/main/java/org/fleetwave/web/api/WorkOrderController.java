package org.fleetwave.web.api;

import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.*;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.fleetwave.web.api.dto.WorkOrderDtos.CreateWorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {
  private final WorkOrderRepository repo;
  private final RadioRepository radios;

  public WorkOrderController(WorkOrderRepository r, RadioRepository radios) {
    this.repo = r;
    this.radios = radios;
  }

  @GetMapping
  public Page<WorkOrder> list(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
    return repo.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkOrder> getById(@PathVariable UUID id) {
    WorkOrder workOrder =
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("WorkOrder not found"));
    return ResponseEntity.ok(workOrder);
  }

  @PostMapping
  public ResponseEntity<WorkOrder> create(@Valid @RequestBody CreateWorkOrder b) {
    WorkOrder w = new WorkOrder();
    w.setId(java.util.UUID.randomUUID());
    Radio r =
        radios
            .findById(b.getRadioId())
            .orElseThrow(() -> new IllegalArgumentException("Radio not found"));
    w.setRadio(r);
    w.setTitle(b.getTitle());
    w.setDescription(b.getDescription());
    w.setDueAt(b.getDueAt());
    w.setCreatedBy(b.getCreatedBy());
    w.setCreatedAt(OffsetDateTime.now());
    w.setStatus(WorkOrder.Status.OPEN);
    return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(w));
  }

  @PutMapping("/{id}")
  public ResponseEntity<WorkOrder> update(@PathVariable UUID id, @RequestBody WorkOrder body) {
    WorkOrder existing =
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("WorkOrder not found"));

    if (body.getTitle() != null) existing.setTitle(body.getTitle());
    if (body.getDescription() != null) existing.setDescription(body.getDescription());
    if (body.getDueAt() != null) existing.setDueAt(body.getDueAt());

    return ResponseEntity.ok(repo.save(existing));
  }

  @PostMapping("/{id}/status")
  public WorkOrder setStatus(@PathVariable java.util.UUID id, @RequestParam String status) {
    WorkOrder w =
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("WorkOrder not found"));
    w.setStatus(WorkOrder.Status.valueOf(status));
    if (w.getStatus() == WorkOrder.Status.DONE) {
      w.setClosedAt(java.time.OffsetDateTime.now());
    }
    return repo.save(w);
  }
}