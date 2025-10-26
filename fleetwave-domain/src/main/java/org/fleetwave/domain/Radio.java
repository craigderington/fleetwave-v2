package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="radio")
public class Radio {
  public enum Status { ACTIVE, SERVICE, RETIRED }

  @Id
  private UUID id;
  private String serial;
  private String model;
  private String callsign;
  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getSerial() { return serial; }
  public void setSerial(String serial) { this.serial = serial; }
  public String getModel() { return model; }
  public void setModel(String model) { this.model = model; }
  public String getCallsign() { return callsign; }
  public void setCallsign(String callsign) { this.callsign = callsign; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

}
