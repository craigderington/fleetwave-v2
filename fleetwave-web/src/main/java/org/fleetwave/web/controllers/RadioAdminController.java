package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.Radio;
import org.fleetwave.domain.repo.RadioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@Controller @RequestMapping("/admin/radios") @RequiredArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN') or hasRole('DISPATCHER')")
public class RadioAdminController {
  private final RadioRepository radios;

  @GetMapping
  public String list(Model model){
    model.addAttribute("radios", radios.findAll());
    return "radios/list";
  }

  @GetMapping("/new")
  public String createForm(Model model){
    model.addAttribute("radio", new Radio());
    return "radios/form";
  }

  @PostMapping
  public String create(@ModelAttribute("radio") @Valid Radio radio, BindingResult br){
    if (radio.getId() == null) radio.setId(UUID.randomUUID());
    if (br.hasErrors()) return "radios/form";
    radios.save(radio);
    return "redirect:/admin/radios";
  }

  @GetMapping("/{id}")
  public String editForm(@PathVariable UUID id, Model model){
    model.addAttribute("radio", radios.findById(id).orElseThrow());
    return "radios/form";
  }
}
