package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="request")
public class Request {
  public enum Status { PENDING, APPROVED, REJECTED, FULFILLED, CANCELLED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="workgroup_id") private Workgroup workgroup;
  private UUID requesterId; private String radioModelPref; private String reason;
  private java.time.OffsetDateTime neededUntil;
  @Enumerated(EnumType.STRING) private Status status = Status.PENDING;
  private java.time.OffsetDateTime createdAt = java.time.OffsetDateTime.now();
  public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
  public Workgroup getWorkgroup(){return workgroup;} public void setWorkgroup(Workgroup w){this.workgroup=w;}
  public UUID getRequesterId(){return requesterId;} public void setRequesterId(UUID r){this.requesterId=r;}
  public String getRadioModelPref(){return radioModelPref;} public void setRadioModelPref(String m){this.radioModelPref=m;}
  public String getReason(){return reason;} public void setReason(String r){this.reason=r;}
  public java.time.OffsetDateTime getNeededUntil(){return neededUntil;} public void setNeededUntil(java.time.OffsetDateTime n){this.neededUntil=n;}
  public Status getStatus(){return status;} public void setStatus(Status s){this.status=s;}
  public java.time.OffsetDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(java.time.OffsetDateTime c){this.createdAt=c;}

}
