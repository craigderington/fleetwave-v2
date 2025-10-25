package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.app.services.FulfillmentService;
import org.fleetwave.app.services.RequestService;
import org.fleetwave.domain.Request;
import org.fleetwave.domain.repo.RequestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller @RequestMapping("/admin/requests") @RequiredArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN') or hasRole('DISPATCHER')")
public class RequestAdminController {
  private final RequestRepository repo;
  private final RequestService service;
  private final FulfillmentService fulfill;

  @GetMapping
  public String list(Model model){
    model.addAttribute("requests", repo.findAll());
    return "requests/list";
  }

  @GetMapping("/new")
  public String form(Model model){
    model.addAttribute("req", new CreateCmd());
    return "requests/form";
  }

  @PostMapping
  public String create(@ModelAttribute CreateCmd cmd){
    service.create(cmd.requesterId, cmd.workgroupId, cmd.radioModelPref, cmd.reason);
    return "redirect:/admin/requests";
  }

  @PostMapping("/{id}/approve")
  public String approve(@PathVariable UUID id, @RequestParam UUID approverId, @RequestParam(required=false) String comment){
    service.approve(id, approverId, comment);
    return "redirect:/admin/requests";
  }

  @PostMapping("/{id}/reject")
  public String reject(@PathVariable UUID id, @RequestParam UUID approverId, @RequestParam(required=false) String comment){
    service.reject(id, approverId, comment);
    return "redirect:/admin/requests";
  }

  @PostMapping("/{id}/fulfill")
  public String fulfill(@PathVariable UUID id){
    fulfill.fulfill(id);
    return "redirect:/admin/requests";
  }

  public static class CreateCmd {
    public UUID requesterId;
    public UUID workgroupId;
    public String radioModelPref;
    public String reason;
  }
}

  @GetMapping("/pending")
  public String pending(Model model){
    model.addAttribute("requests", repo.findAll().stream().filter(r -> r.getStatus() == org.fleetwave.domain.Request.Status.PENDING).toList());
    return "requests/list";
  }
