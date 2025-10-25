package org.fleetwave.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fleetwave.domain.IdempotencyKey;
import org.fleetwave.domain.repo.IdempotencyKeyRepository;
import org.fleetwave.infra.tenant.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {
  private final IdempotencyKeyRepository repo;
  public IdempotencyFilter(IdempotencyKeyRepository repo){ this.repo = repo; }

  @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc)
      throws ServletException, IOException {
    if ("POST".equalsIgnoreCase(req.getMethod()) && req.getRequestURI().startsWith("/api/")) {
      String key = req.getHeader("Idempotency-Key");
      if (key != null && !key.isBlank()) {
        var exists = repo.findByKey(key).isPresent();
        if (exists) { res.setStatus(208); return; } // Already Reported
        var idk = IdempotencyKey.builder().id(UUID.randomUUID()).key(key).tenantId(TenantContext.get()).build();
        repo.save(idk);
      }
    }
    fc.doFilter(req, res);
  }
}
