package org.fleetwave.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Reads DB_SECRET_JSON env (from AWS Secrets Manager) and injects spring.datasource.* properties.
 * Expected JSON: {"username":"...","password":"...","database":"fleetwave","host":"...","port":5432}
 */
public class DbSecretEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
  @Override public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
    String json = System.getenv("DB_SECRET_JSON");
    if (json == null || json.isBlank()) return;
    try {
      Map<String, Object> props = new HashMap<>();
      Map<String, Object> parsed = parse(json);
      String url = "jdbc:postgresql://" + parsed.get("host") + ":" + parsed.get("port") + "/" + parsed.get("database");
      props.put("spring.datasource.url", url);
      props.put("spring.datasource.username", parsed.get("username"));
      props.put("spring.datasource.password", parsed.get("password"));
      env.getPropertySources().addFirst(new MapPropertySource("dbSecret", props));
    } catch (Exception e) {
      System.err.println("Failed to parse DB_SECRET_JSON: " + e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String,Object> parse(String json) {
    // tiny JSON parser for simple flat dict to avoid extra deps
    Map<String,Object> map = new HashMap<>();
    String s = json.trim();
    if (s.startsWith("{") && s.endsWith("}")) s = s.substring(1, s.length()-1);
    for (String part : s.split(",")) {
      String[] kv = part.split(":", 2);
      if (kv.length != 2) continue;
      String k = kv[0].trim().replaceAll("^["']|["']$", "");
      String v = kv[1].trim();
      if (v.matches("^".*"$")) {
        v = v.substring(1, v.length()-1);
        map.put(k, v);
      } else if (v.matches("^[0-9]+$")) {
        map.put(k, Integer.parseInt(v));
      } else {
        map.put(k, v.replaceAll("^["']|["']$", ""));
      }
    }
    return map;
  }

  @Override public int getOrder() { return Ordered.HIGHEST_PRECEDENCE + 10; }
}
