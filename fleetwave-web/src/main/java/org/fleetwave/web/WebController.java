package org.fleetwave.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Controller for server-side rendered Thymeleaf pages.
 * Handles public pages like landing, login, and status.
 */
@Controller
public class WebController {

    @Autowired(required = false)
    private HealthEndpoint healthEndpoint;

    /**
     * Landing page - shows FleetWave welcome and features
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Welcome");
        return "index";
    }

    /**
     * Login page - Spring Security will handle the actual authentication
     * This mapping is for displaying the login form
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * System status page - shows application health and version info
     */
    @GetMapping("/status")
    public String status(Model model) {
        model.addAttribute("pageTitle", "System Status");

        // Get current timestamp
        String currentTime = ZonedDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        model.addAttribute("currentTime", currentTime);

        // Get health status from actuator if available
        if (healthEndpoint != null) {
            var health = healthEndpoint.health();
            model.addAttribute("healthStatus", health.getStatus().getCode());

            // Try to get components if available (only for CompositeHealth)
            if (health instanceof org.springframework.boot.actuate.health.CompositeHealth compositeHealth) {
                model.addAttribute("healthDetails", compositeHealth.getComponents());
            }

            // Determine status badge color
            String statusClass = switch (health.getStatus().getCode()) {
                case "UP" -> "success";
                case "DOWN" -> "danger";
                case "OUT_OF_SERVICE" -> "warning";
                default -> "secondary";
            };
            model.addAttribute("statusClass", statusClass);
        } else {
            model.addAttribute("healthStatus", "UNKNOWN");
            model.addAttribute("statusClass", "secondary");
        }

        // Application info
        model.addAttribute("appVersion", "0.4.0-SNAPSHOT");
        model.addAttribute("appName", "FleetWave");

        return "status";
    }

    /**
     * Error page handler
     */
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("pageTitle", "Error");
        return "error";
    }
}
