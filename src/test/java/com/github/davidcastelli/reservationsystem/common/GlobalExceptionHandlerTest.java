package com.github.davidcastelli.reservationsystem.common;

import static org.assertj.core.api.Assertions.*;

import com.github.davidcastelli.reservationsystem.common.exception.GroupNotFoundException;
import com.github.davidcastelli.reservationsystem.common.exception.InvalidRequestIdException;
import java.net.URI;
import java.util.Map;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock private WebRequest request;

  @Test
  void givenBadRequestException_whenHandleBadRequestException_thenReturnCorrectProblemDetail() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    ErrorDetail[] errors =
        new ErrorDetail[] {
          new ErrorDetail("Request.InvalidRequestId", "The request id must match the route id.")
        };
    InvalidRequestIdException invalidRequestIdException = new InvalidRequestIdException(errors);

    ProblemDetail actual =
        globalExceptionHandler.handleBadRequestException(invalidRequestIdException, request);

    ProblemDetail expected =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, invalidRequestIdException.getMessage());
    expected.setType(URI.create("https://tools.ietf.org/html/rfc9110#section-15.5.1"));
    expected.setProperties(
        Map.of(
            "errors",
            Map.of(
                "Request.InvalidRequestId",
                new String[] {"The request id must match the route id."})));

    assertThat(actual).isNotNull().usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void givenNotFoundException_whenHandleNotFoundException_thenReturnCorrectProblemDetail() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    GroupNotFoundException groupNotFoundException = new GroupNotFoundException(1);

    ProblemDetail actual =
        globalExceptionHandler.handleNotFoundException(groupNotFoundException, request);

    ProblemDetail expected =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, groupNotFoundException.getMessage());
    expected.setType(URI.create("https://tools.ietf.org/html/rfc9110#section-15.5.5"));

    assertThat(actual).isNotNull().usingRecursiveComparison().isEqualTo(expected);
  }
}
