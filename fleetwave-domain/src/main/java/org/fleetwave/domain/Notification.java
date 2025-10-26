package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="notification")
public class Notification {
  public enum Channel { EMAIL, SMS, WEBHOOK }
  public enum Status { PENDING, SENT, FAILED }

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name="alert_id")
  private Alert alert;

  @Enumerated(EnumType.STRING)
  private Channel channel = Channel.EMAIL;

  private String destination;

  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;

  private int attempts = 0;
  private OffsetDateTime lastAttemptAt;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Alert getAlert() { return alert; }
  public void setAlert(Alert alert) { this.alert = alert; }
  public Channel getChannel() { return channel; }
  public void setChannel(Channel channel) { this.channel = channel; }
  public String getDestination() { return destination; }
  public void setDestination(String destination) { this.destination = destination; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }
  public int getAttempts() { return attempts; }
  public void setAttempts(int attempts) { this.attempts = attempts; }
  public OffsetDateTime getLastAttemptAt() { return lastAttemptAt; }
  public void setLastAttemptAt(OffsetDateTime lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }

}
