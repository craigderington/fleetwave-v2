package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.repo.AlertRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/admin/alerts") @RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('DISPATCHER') or hasRole('SUPERVISOR')")
public class AlertAdminController {
  private final AlertRepository alerts;

  @GetMapping
  public String inbox(Model model){
    model.addAttribute("alerts", alerts.findAll());
    return "alerts/inbox";
  }
}
