package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.app.services.AssignmentService;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.repo.RadioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Controller @RequestMapping("/admin/assignments") @RequiredArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN') or hasRole('DISPATCHER')")
public class AssignmentAdminController {
  private final AssignmentService service;
  private final RadioRepository radios;

  @GetMapping("/new")
  public String form(Model model){
    model.addAttribute("radios", radios.findAll());
    model.addAttribute("cmd", new CreateCmd());
    return "assignments/form";
  }

  @PostMapping
  public String create(@ModelAttribute CreateCmd cmd){
    service.create(cmd.radioId, cmd.assigneeType, cmd.assigneeId, cmd.expectedEnd);
    return "redirect:/admin";
  }

  public static class CreateCmd {
    public UUID radioId;
    public Assignment.AssigneeType assigneeType = Assignment.AssigneeType.WORKGROUP;
    public UUID assigneeId;
    public OffsetDateTime expectedEnd;
  }
}
