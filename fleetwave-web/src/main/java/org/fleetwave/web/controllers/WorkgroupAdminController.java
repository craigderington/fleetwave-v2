package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.Department;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.WorkgroupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller @RequestMapping("/admin/workgroups") @RequiredArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN') or hasRole('DISPATCHER')")
public class WorkgroupAdminController {
  private final WorkgroupRepository workgroups;

  @GetMapping("/new")
  public String form(Model model){
    model.addAttribute("workgroup", new Workgroup());
    return "workgroups/form";
  }

  @PostMapping
  public String create(@ModelAttribute Workgroup wg){
    if (wg.getId() == null) wg.setId(UUID.randomUUID());
    if (wg.getDepartment() == null || wg.getDepartment().getId() == null){
      // quick placeholder: attach a dummy department id (replace with selector UI)
      wg.setDepartment(Department.builder().id(UUID.fromString("11111111-1111-1111-1111-111111111111")).build());
    }
    workgroups.save(wg);
    return "redirect:/admin";
  }
}
