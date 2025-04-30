package com.github.davidcastelli.reservation_system.common.exception;

/**
 * Base class for not found exceptions. Exceptions deriving from this class are mapped to a {@link
 * org.springframework.http.ProblemDetail ProblemDetail} with status code 404 and are handled by
 * {@link com.github.davidcastelli.reservation_system.common.GlobalExceptionHandler
 * GlobalExceptionHandler}.
 */
public abstract class NotFoundException extends RuntimeException {
  /**
   * Creates a {@link NotFoundException NotFoundException}.
   *
   * @param message The error message.
   */
  protected NotFoundException(String message) {
    super(message);
  }
}
