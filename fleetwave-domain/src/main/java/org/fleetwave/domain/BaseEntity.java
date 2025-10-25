package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

@MappedSuperclass
@Getter @Setter
@FilterDef(name = "tenant", parameters = @ParamDef(name = "tenantId", type = UUID.class))
@Filter(name = "tenant", condition = "tenant_id = :tenantId")
public abstract class BaseEntity {
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;
}
