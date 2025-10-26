package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="notification")
public class Notification {
  public enum Channel { EMAIL, SMS, WEBHOOK }
  public enum Status { PENDING, SENT, FAILED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="alert_id") private Alert alert;
  @Enumerated(EnumType.STRING) private Channel channel = Channel.EMAIL;
  private String destination;
  @Enumerated(EnumType.STRING) private Status status = Status.PENDING;
  private int attempts = 0; private java.time.OffsetDateTime lastAttemptAt;
  public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
  public Alert getAlert(){return alert;} public void setAlert(Alert a){this.alert=a;}
  public Channel getChannel(){return channel;} public void setChannel(Channel c){this.channel=c;}
  public String getDestination(){return destination;} public void setDestination(String d){this.destination=d;}
  public Status getStatus(){return status;} public void setStatus(Status s){this.status=s;}
  public int getAttempts(){return attempts;} public void setAttempts(int a){this.attempts=a;}
  public java.time.OffsetDateTime getLastAttemptAt(){return lastAttemptAt;} public void setLastAttemptAt(java.time.OffsetDateTime l){this.lastAttemptAt=l;}

}
