package org.fleetwave.domain.repo;
import org.fleetwave.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
  List<UserRole> findByUser_Id(UUID userId);
}
