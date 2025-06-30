package com.github.davidcastelli.reservationsystem.common.exception;

import com.github.davidcastelli.reservationsystem.common.ErrorDetail;

/**
 * Exception which is thrown when the request id is invalid. A request id is invalid when the id in
 * the request body does not match the id in the route.
 */
public class InvalidRequestIdException extends BadRequestException {
  /**
   * Creates a {@link InvalidRequestIdException InvalidRequestIdException}.
   *
   * @param errors An array of errors details describing what went wrong with the request.
   */
  public InvalidRequestIdException(ErrorDetail[] errors) {
    super("Request validation failed.", errors);
  }
}
