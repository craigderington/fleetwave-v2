package org.fleetwave.app.schedulers;

import java.time.OffsetDateTime;
import java.util.List;
import org.fleetwave.domain.Assignment;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueScanner {

    private static final Logger log = LoggerFactory.getLogger(OverdueScanner.class);
    private final AssignmentRepository assignments;

    public OverdueScanner(AssignmentRepository assignments) {
        this.assignments = assignments;
    }

    @Scheduled(cron = "0 */15 * * * *") // every 15 minutes
    public void scan() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Assignment> overdue = assignments.findOverdue(now);
        if (!overdue.isEmpty()) {
            log.info("Found {} overdue assignments", overdue.size());
        }
    }
}
