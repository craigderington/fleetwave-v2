package org.fleetwave.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TenantConfigApi {
  @GetMapping("/api/v1/tenant/ui-config")
  public Map<String,Object> ui(){
    // For MVP return constants; later pull from DB per-tenant config
    return Map.of("logoUrl", "/static/logo.svg", "primaryColor", "#0d6efd", "brand", "FleetWave");
  }
}
