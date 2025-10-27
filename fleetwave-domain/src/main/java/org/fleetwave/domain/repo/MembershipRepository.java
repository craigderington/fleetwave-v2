package org.fleetwave.domain.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fleetwave.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {
  List<Membership> findAllByTenantIdAndWorkgroup_Id(String tenantId, UUID workgroupId);
  List<Membership> findAllByTenantIdAndPerson_Id(String tenantId, UUID personId);
  Optional<Membership> findByTenantIdAndPerson_IdAndWorkgroup_Id(String tenantId, UUID personId, UUID workgroupId);
}
