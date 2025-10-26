package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="assignment")
public class Assignment {
  public enum Status { ACTIVE, RETURNED, CANCELLED }
  public enum AssigneeType { USER, WORKGROUP }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="radio_id") private Radio radio;
  @Enumerated(EnumType.STRING) private AssigneeType assigneeType;
  private UUID assigneeId;
  private java.time.OffsetDateTime startAt = java.time.OffsetDateTime.now();
  private java.time.OffsetDateTime expectedEnd; private java.time.OffsetDateTime endAt;
  @Enumerated(EnumType.STRING) private Status status = Status.ACTIVE;
  public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
  public Radio getRadio(){return radio;} public void setRadio(Radio radio){this.radio=radio;}
  public AssigneeType getAssigneeType(){return assigneeType;} public void setAssigneeType(AssigneeType a){this.assigneeType=a;}
  public UUID getAssigneeId(){return assigneeId;} public void setAssigneeId(UUID a){this.assigneeId=a;}
  public java.time.OffsetDateTime getStartAt(){return startAt;} public void setStartAt(java.time.OffsetDateTime s){this.startAt=s;}
  public java.time.OffsetDateTime getExpectedEnd(){return expectedEnd;} public void setExpectedEnd(java.time.OffsetDateTime e){this.expectedEnd=e;}
  public java.time.OffsetDateTime getEndAt(){return endAt;} public void setEndAt(java.time.OffsetDateTime e){this.endAt=e;}
  public Status getStatus(){return status;} public void setStatus(Status s){this.status=s;}

}
