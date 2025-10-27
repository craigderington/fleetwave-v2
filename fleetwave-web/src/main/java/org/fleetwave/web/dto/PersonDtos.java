package org.fleetwave.web.dto;

public final class PersonDtos {
  private PersonDtos() {}

  public static final class CreatePersonRequest {
    public String tenantId;
    public String externalRef;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
  }

  public static final class UpdatePersonRequest {
    public String externalRef;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String status; // ACTIVE / INACTIVE
  }
}
