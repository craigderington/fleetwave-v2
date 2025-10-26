package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="workorder")
public class WorkOrder {
  public enum Status { OPEN, IN_PROGRESS, DONE, CANCELLED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="radio_id") private Radio radio;
  private String title;
  @Column(length=4000) private String description;
  private java.time.OffsetDateTime createdAt = java.time.OffsetDateTime.now();
  private java.time.OffsetDateTime dueAt;
  private java.time.OffsetDateTime closedAt;
  @Enumerated(EnumType.STRING) private Status status = Status.OPEN;
  private String createdBy;
  public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
  public Radio getRadio(){return radio;} public void setRadio(Radio radio){this.radio=radio;}
  public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
  public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
  public java.time.OffsetDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(java.time.OffsetDateTime c){this.createdAt=c;}
  public java.time.OffsetDateTime getDueAt(){return dueAt;} public void setDueAt(java.time.OffsetDateTime d){this.dueAt=d;}
  public java.time.OffsetDateTime getClosedAt(){return closedAt;} public void setClosedAt(java.time.OffsetDateTime c){this.closedAt=c;}
  public Status getStatus(){return status;} public void setStatus(Status s){this.status=s;}
  public String getCreatedBy(){return createdBy;} public void setCreatedBy(String createdBy){this.createdBy=createdBy;}

}
