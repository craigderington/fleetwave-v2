package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="workgroup")
public class Workgroup {
  @Id private UUID id; private String name;
  public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}

}
