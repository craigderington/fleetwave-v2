package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="alert")
public class Alert {
  public enum Status { OPEN, ACK, CLOSED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="rule_id") private AlertRule rule;
  private String subjectType; private UUID subjectId;
  @Enumerated(EnumType.STRING) private Status status = Status.OPEN;
  private java.time.OffsetDateTime firstSeen = java.time.OffsetDateTime.now();
  private java.time.OffsetDateTime lastSeen = java.time.OffsetDateTime.now();
  private int count = 1;
  public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
  public AlertRule getRule(){return rule;} public void setRule(AlertRule r){this.rule=r;}
  public String getSubjectType(){return subjectType;} public void setSubjectType(String s){this.subjectType=s;}
  public UUID getSubjectId(){return subjectId;} public void setSubjectId(UUID s){this.subjectId=s;}
  public Status getStatus(){return status;} public void setStatus(Status st){this.status=st;}
  public java.time.OffsetDateTime getFirstSeen(){return firstSeen;} public void setFirstSeen(java.time.OffsetDateTime f){this.firstSeen=f;}
  public java.time.OffsetDateTime getLastSeen(){return lastSeen;} public void setLastSeen(java.time.OffsetDateTime l){this.lastSeen=l;}
  public int getCount(){return count;} public void setCount(int c){this.count=c;}

}
