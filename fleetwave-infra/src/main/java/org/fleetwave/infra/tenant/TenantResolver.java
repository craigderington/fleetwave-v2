package org.fleetwave.infra.tenant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantResolver {
  private final JdbcTemplate jdbc;
  private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
  private final long ttlMs = 5 * 60 * 1000; // 5 minutes

  public TenantResolver(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public Optional<UUID> resolveKey(String key){
    if (key == null || key.isBlank()) return Optional.empty();
    CacheEntry c = cache.get(key);
    if (c != null && (Instant.now().toEpochMilli() - c.at) < ttlMs) return Optional.of(c.id);
    try {
      UUID id = jdbc.query("select id from tenant where key = ?", rs -> rs.next() ? UUID.fromString(rs.getString(1)) : null, key);
      if (id != null) {
        cache.put(key, new CacheEntry(id));
        return Optional.of(id);
      }
    } catch (Exception ignored) {}
    return Optional.empty();
  }

  private record CacheEntry(UUID id){ long at = Instant.now().toEpochMilli(); }
}
