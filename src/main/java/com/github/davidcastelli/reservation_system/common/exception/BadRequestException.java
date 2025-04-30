package com.github.davidcastelli.reservation_system.common.exception;

import com.github.davidcastelli.reservation_system.common.ErrorDetail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for bad request exceptions. Exceptions deriving from this class are mapped to a {@link
 * org.springframework.http.ProblemDetail ProblemDetail} with status code 400 and are handled by
 * {@link com.github.davidcastelli.reservation_system.common.GlobalExceptionHandler
 * GlobalExceptionHandler}. Bad request exceptions deriving from this class extend problem details
 * with an error field. The error field contains a map of error details providing information on
 * what went wrong with the request.
 */
public abstract class BadRequestException extends RuntimeException {
  /** An array of errors describing what went wrong with the request. */
  private final ErrorDetail[] errors;

  /**
   * Creates a {@link BadRequestException BadRequestException}.
   *
   * @param message The error message.
   */
  protected BadRequestException(String message) {
    super(message);
    this.errors = new ErrorDetail[] {};
  }

  /**
   * Creates a {@link BadRequestException BadRequestException}.
   *
   * @param message The error message.
   * @param errors An array of {@link ErrorDetail ErrorDetails} describing what went wrong with the
   *     request.
   */
  protected BadRequestException(String message, ErrorDetail[] errors) {
    super(message);
    this.errors = errors;
  }

  /**
   * Gets the array of {@link ErrorDetail ErrorDetails} as a map with the error code as the key and
   * the values as an array of all error descriptions belonging to the error code.
   *
   * @return A map of error codes and their corresponding error descriptions.
   */
  public Map<String, String[]> getErrors() {
    var hashMap = new HashMap<String, String[]>();

    for (var error : errors) {
      if (hashMap.containsKey(error.code())) {
        var oldErrors = hashMap.get(error.code());
        var newErrors = Arrays.copyOf(oldErrors, oldErrors.length + 1);
        newErrors[oldErrors.length] = error.description();
        hashMap.put(error.code(), newErrors);
      } else {
        hashMap.put(error.code(), new String[] {error.description()});
      }
    }

    return hashMap;
  }
}
