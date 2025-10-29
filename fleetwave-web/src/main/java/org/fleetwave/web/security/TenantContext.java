package org.fleetwave.web.security;

/**
 * Holds the current tenant ID in a ThreadLocal for the current request.
 * Used by filters and services to access tenant context.
 */
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext() {
        // Private constructor to prevent instantiation
    }

    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}
