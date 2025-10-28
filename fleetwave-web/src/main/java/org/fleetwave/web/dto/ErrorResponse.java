package org.fleetwave.web.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Standardized error response for all API exceptions.
 * Returns consistent error structure with HTTP status, message, and optional field errors.
 */
public class ErrorResponse {

  private OffsetDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
  private List<FieldError> fieldErrors;

  public ErrorResponse() {
    this.timestamp = OffsetDateTime.now();
    this.fieldErrors = new ArrayList<>();
  }

  public ErrorResponse(int status, String error, String message, String path) {
    this();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  public static class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;

    public FieldError() {}

    public FieldError(String field, String message) {
      this.field = field;
      this.message = message;
    }

    public FieldError(String field, String message, Object rejectedValue) {
      this.field = field;
      this.message = message;
      this.rejectedValue = rejectedValue;
    }

    public String getField() {
      return field;
    }

    public void setField(String field) {
      this.field = field;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Object getRejectedValue() {
      return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
      this.rejectedValue = rejectedValue;
    }
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<FieldError> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldError> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

  public void addFieldError(String field, String message) {
    this.fieldErrors.add(new FieldError(field, message));
  }

  public void addFieldError(String field, String message, Object rejectedValue) {
    this.fieldErrors.add(new FieldError(field, message, rejectedValue));
  }
}
