package org.fleetwave.domain.repo;

import org.fleetwave.domain.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ApprovalRepository extends JpaRepository<Approval, UUID> { }
