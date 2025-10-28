package org.fleetwave.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.fleetwave.web.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for all REST controllers.
 * Catches exceptions and returns consistent ErrorResponse DTOs with appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handle validation errors from @Valid annotation.
   * Returns 400 Bad Request with field-level error details.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    BindingResult bindingResult = ex.getBindingResult();
    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Invalid request parameters",
            request.getRequestURI());

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      error.addFieldError(
          fieldError.getField(),
          fieldError.getDefaultMessage(),
          fieldError.getRejectedValue());
    }

    log.warn("Validation failed for {}: {} field errors", request.getRequestURI(), error.getFieldErrors().size());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handle IllegalArgumentException (bad input parameters).
   * Returns 400 Bad Request.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI());

    log.warn("Bad request to {}: {}", request.getRequestURI(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handle IllegalStateException (business rule violations).
   * Returns 409 Conflict.
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(
      IllegalStateException ex, HttpServletRequest request) {

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            ex.getMessage(),
            request.getRequestURI());

    log.warn("State conflict at {}: {}", request.getRequestURI(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  /**
   * Handle NoSuchElementException (entity not found).
   * Returns 404 Not Found.
   */
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(
      NoSuchElementException ex, HttpServletRequest request) {

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage() != null ? ex.getMessage() : "Resource not found",
            request.getRequestURI());

    log.warn("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Handle all other exceptions.
   * Returns 500 Internal Server Error (without exposing stack trace).
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request) {

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please contact support if the problem persists.",
            request.getRequestURI());

    // Log full stack trace for debugging, but don't expose to client
    log.error("Unexpected error at {}", request.getRequestURI(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
