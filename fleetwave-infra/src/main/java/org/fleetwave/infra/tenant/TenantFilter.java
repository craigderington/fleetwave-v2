package org.fleetwave.infra.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class TenantFilter extends OncePerRequestFilter {
  private final TenantResolver resolver;

  public TenantFilter(TenantResolver resolver) { this.resolver = resolver; }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    UUID tenant = resolveTenant(request);
    TenantContext.set(tenant);
    try { filterChain.doFilter(request, response); }
    finally { TenantContext.clear(); }
  }

  private UUID resolveTenant(HttpServletRequest req){
    // 1) Header override (UUID form)
    String hdr = req.getHeader("X-Tenant-Id");
    if (hdr != null && !hdr.isBlank()) {
      try { return UUID.fromString(hdr); } catch (IllegalArgumentException ignored) {}
    }
    // 2) Subdomain mapping: e.g. ocps.fleetwave.org -> key 'ocps'
    String host = Optional.ofNullable(req.getHeader("Host")).orElse(req.getServerName());
    String key = extractSubdomainKey(host);
    if (key != null) {
      return resolver.resolveKey(key).orElse(DEFAULT_DEV_TENANT);
    }
    return DEFAULT_DEV_TENANT;
  }

  private String extractSubdomainKey(String host){
    if (host == null) return null;
    host = host.split(":")[0];
    String[] parts = host.split("\.");
    if (parts.length < 3) return null; // expect subdomain.domain.tld
    return parts[0]; // 'ocps' from 'ocps.fleetwave.org'
  }

  private static final UUID DEFAULT_DEV_TENANT = UUID.fromString("00000000-0000-0000-0000-000000000001");
}
