package org.fleetwave.infra.tenant;

import java.util.UUID;

public final class TenantContext {
  private static final ThreadLocal<UUID> CURRENT = new ThreadLocal<>();
  public static void set(UUID id){ CURRENT.set(id); }
  public static UUID get(){ return CURRENT.get(); }
  public static void clear(){ CURRENT.remove(); }
}
