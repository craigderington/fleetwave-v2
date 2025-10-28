package org.fleetwave.web.api;

import jakarta.validation.Valid;
import java.util.*;
import org.fleetwave.app.services.RequestService;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.RequestRepository;
import org.fleetwave.web.api.dto.RequestDtos.CreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
  private final RequestService svc;
  private final RequestRepository repo;

  public RequestController(RequestService s, RequestRepository r) {
    svc = s;
    repo = r;
  }

  @GetMapping
  public Page<Request> list(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
    return repo.findAll(pageable);
  }

  @PostMapping
  public Request create(@Valid @RequestBody CreateRequest b) {
    return svc.create(b.getRequesterId(), b.getWorkgroupId(), b.getModelPref(), b.getReason());
  }

  @PostMapping("/{id}/approve")
  public Request approve(@PathVariable java.util.UUID id) {
    return svc.approve(id);
  }

  @PostMapping("/{id}/reject")
  public Request reject(@PathVariable java.util.UUID id) {
    return svc.reject(id);
  }
}