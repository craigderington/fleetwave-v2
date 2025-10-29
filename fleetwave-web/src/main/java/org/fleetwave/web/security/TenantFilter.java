package org.fleetwave.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that extracts tenant ID from request and sets it in TenantContext.
 * Tenant resolution strategy:
 * 1. Check X-Tenant header (for API requests)
 * 2. Check session attribute (for authenticated web users)
 * 3. Default to 'ocps' for demo purposes
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant";
    private static final String TENANT_SESSION_ATTRIBUTE = "tenantId";
    private static final String DEFAULT_TENANT = "ocps";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantId = resolveTenantId(request);
            TenantContext.setTenantId(tenantId);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String resolveTenantId(HttpServletRequest request) {
        // 1. Try X-Tenant header (for API requests)
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            return tenantId;
        }

        // 2. Try session attribute (for authenticated web users)
        if (request.getSession(false) != null) {
            Object sessionTenant = request.getSession().getAttribute(TENANT_SESSION_ATTRIBUTE);
            if (sessionTenant != null) {
                return sessionTenant.toString();
            }
        }

        // 3. Default tenant for demo
        return DEFAULT_TENANT;
    }
}
