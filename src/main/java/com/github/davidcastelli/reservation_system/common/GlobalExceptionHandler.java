package com.github.davidcastelli.reservation_system.common;

import com.github.davidcastelli.reservation_system.common.exception.BadRequestException;
import com.github.davidcastelli.reservation_system.common.exception.NotFoundException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** A global exception handler used to handle controller exceptions in a centralized location. */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles bad request exceptions by returning a problem detail to the client.
   *
   * @param bre The bad request exception.
   * @param request The web request.
   * @return A {@link ProblemDetail ProblemDetail} with status code 400.
   */
  @ExceptionHandler(BadRequestException.class)
  public ProblemDetail handleBadRequestException(BadRequestException bre, WebRequest request) {
    var errors = bre.getErrors().values().stream().flatMap(Arrays::stream).toList();
    LOGGER.error(
        "Bad request exception with status 400 has occurred: {}, Request Details: {}, Errors: {}",
        bre.getMessage(),
        request.getDescription(false),
        errors,
        bre);

    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, bre.getMessage());
    URI type = URI.create("https://tools.ietf.org/html/rfc9110#section-15.5.1");
    problemDetail.setType(type);
    problemDetail.setProperties(Map.of("errors", bre.getErrors()));
    return problemDetail;
  }

  /**
   * Handles not found exceptions by returning a problem detail to the client.
   *
   * @param nfe The not found exception.
   * @param request The web request.
   * @return A {@link ProblemDetail ProblemDetail} with status code 404.
   */
  @ExceptionHandler(NotFoundException.class)
  public ProblemDetail handleNotFoundException(NotFoundException nfe, WebRequest request) {
    LOGGER.error(
        "Not found exception with status 404 has occurred: {}, Request Details: {}",
        nfe.getMessage(),
        request.getDescription(false),
        nfe);

    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, nfe.getMessage());
    URI type = URI.create("https://tools.ietf.org/html/rfc9110#section-15.5.5");
    problemDetail.setType(type);
    return problemDetail;
  }
}
