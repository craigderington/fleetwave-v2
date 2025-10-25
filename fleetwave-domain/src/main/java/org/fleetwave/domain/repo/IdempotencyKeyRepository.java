package org.fleetwave.domain.repo;
import org.fleetwave.domain.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, UUID> {
  Optional<IdempotencyKey> findByKey(String key);
}
