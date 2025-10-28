package org.fleetwave.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class PersonDtos {
  private PersonDtos() {}

  public static final class CreatePersonRequest {
    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @Email(message = "email must be valid")
    private String email;

    private String phone;
    private String externalRef;

    public CreatePersonRequest() {}

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getExternalRef() {
      return externalRef;
    }

    public void setExternalRef(String externalRef) {
      this.externalRef = externalRef;
    }
  }

  public static final class UpdatePersonRequest {
    private String firstName;
    private String lastName;

    @Email(message = "email must be valid")
    private String email;

    private String phone;
    private String externalRef;
    private String status; // ACTIVE / INACTIVE

    public UpdatePersonRequest() {}

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getExternalRef() {
      return externalRef;
    }

    public void setExternalRef(String externalRef) {
      this.externalRef = externalRef;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }
}
