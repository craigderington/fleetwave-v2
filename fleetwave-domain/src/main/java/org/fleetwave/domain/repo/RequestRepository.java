package org.fleetwave.domain.repo;

import org.fleetwave.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
  List<Request> findByRequesterId(UUID requesterId);
}
