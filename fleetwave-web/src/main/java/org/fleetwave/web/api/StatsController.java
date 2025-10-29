package org.fleetwave.web.api;

import org.fleetwave.domain.repo.*;
import org.fleetwave.web.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API for dashboard statistics and metrics.
 * Provides aggregated data for admin dashboard visualizations.
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private RadioRepository radioRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WorkgroupRepository workgroupRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    /**
     * Get dashboard overview statistics
     */
    @GetMapping("/overview")
    public Map<String, Object> getOverviewStats() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            tenantId = "ocps"; // Fallback for demo
        }

        Map<String, Object> stats = new HashMap<>();

        // Radio statistics
        long totalRadios = radioRepository.findAllByTenantId(tenantId).size();
        long availableRadios = radioRepository.findAllByTenantId(tenantId).stream()
            .filter(r -> "AVAILABLE".equals(r.getStatus()))
            .count();
        long assignedRadios = radioRepository.findAllByTenantId(tenantId).stream()
            .filter(r -> "ASSIGNED".equals(r.getStatus()))
            .count();
        long inRepairRadios = radioRepository.findAllByTenantId(tenantId).stream()
            .filter(r -> "IN_REPAIR".equals(r.getStatus()))
            .count();

        Map<String, Long> radioStats = new HashMap<>();
        radioStats.put("total", totalRadios);
        radioStats.put("available", availableRadios);
        radioStats.put("assigned", assignedRadios);
        radioStats.put("inRepair", inRepairRadios);
        stats.put("radios", radioStats);

        // Personnel statistics
        long totalPersons = personRepository.findAllByTenantId(tenantId).size();
        long enabledPersons = personRepository.findAllByTenantId(tenantId).stream()
            .filter(p -> p.getEnabled() != null && p.getEnabled())
            .count();

        Map<String, Long> personStats = new HashMap<>();
        personStats.put("total", totalPersons);
        personStats.put("enabled", enabledPersons);
        stats.put("persons", personStats);

        // Workgroup statistics
        long totalWorkgroups = workgroupRepository.findAllByTenantId(tenantId).size();
        stats.put("workgroups", Map.of("total", totalWorkgroups));

        // Assignment statistics
        long totalAssignments = assignmentRepository.findAllByTenantId(tenantId, org.springframework.data.domain.Pageable.unpaged()).getTotalElements();
        long activeAssignments = assignmentRepository.findAllByTenantId(tenantId, org.springframework.data.domain.Pageable.unpaged())
            .stream()
            .filter(a -> "ASSIGNED".equals(a.getStatus().toString()))
            .count();

        Map<String, Long> assignmentStats = new HashMap<>();
        assignmentStats.put("total", totalAssignments);
        assignmentStats.put("active", activeAssignments);
        stats.put("assignments", assignmentStats);

        return stats;
    }

    /**
     * Get radio status distribution for pie chart
     */
    @GetMapping("/radios/by-status")
    public Map<String, Long> getRadiosByStatus() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            tenantId = "ocps";
        }

        Map<String, Long> distribution = new HashMap<>();
        var radios = radioRepository.findAllByTenantId(tenantId);

        distribution.put("AVAILABLE", radios.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count());
        distribution.put("ASSIGNED", radios.stream().filter(r -> "ASSIGNED".equals(r.getStatus())).count());
        distribution.put("IN_REPAIR", radios.stream().filter(r -> "IN_REPAIR".equals(r.getStatus())).count());
        distribution.put("RETIRED", radios.stream().filter(r -> "RETIRED".equals(r.getStatus())).count());

        return distribution;
    }
}
