package com.github.davidcastelli.reservationsystem.common.utility;

import com.github.davidcastelli.reservationsystem.common.ErrorDetail;

/** Utility class used to create error details for invalid requests. */
public final class RequestErrors {

  private RequestErrors() {}

  /**
   * Creates an error detail for when the request id is invalid.
   *
   * @return An {@link ErrorDetail ErrorDetail}.
   */
  public static ErrorDetail InvalidRequestId() {
    return new ErrorDetail("Request.InvalidRequestId", "The request id must match the route id.");
  }
}
