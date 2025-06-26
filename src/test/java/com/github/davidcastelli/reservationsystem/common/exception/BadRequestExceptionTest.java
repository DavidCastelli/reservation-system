package com.github.davidcastelli.reservationsystem.common.exception;

import static org.assertj.core.api.Assertions.*;

import com.github.davidcastelli.reservationsystem.common.ErrorDetail;
import java.util.AbstractMap;
import java.util.Map;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@NullUnmarked
class BadRequestExceptionTest {

  @Test
  void givenBadRequestException_whenGetMessage_thenReturnCorrectMessage() {
    BadRequestException badRequestException =
        Mockito.mock(
            BadRequestException.class,
            Mockito.withSettings()
                .useConstructor("Bad request exception.")
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    String message = badRequestException.getMessage();

    assertThat(message).isNotNull().isEqualTo("Bad request exception.");
  }

  @Test
  void givenBadRequestExceptionWithEmptyErrors_whenGetErrors_thenReturnEmptyMap() {
    BadRequestException badRequestException =
        Mockito.mock(
            BadRequestException.class,
            Mockito.withSettings()
                .useConstructor("Bad request exception.", new ErrorDetail[] {})
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    Map<String, String[]> errors = badRequestException.getErrors();

    assertThat(errors).isNotNull().isEmpty();
  }

  @Test
  void givenBadRequestExceptionWithOneError_whenGetErrors_thenReturnCorrectSizeAndError() {
    BadRequestException badRequestException =
        Mockito.mock(
            BadRequestException.class,
            Mockito.withSettings()
                .useConstructor(
                    "Bad request exception.",
                    new ErrorDetail[] {
                      new ErrorDetail(
                          "Request.InvalidRequestId", "The request id must match the route id.")
                    })
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    Map<String, String[]> errors = badRequestException.getErrors();

    assertThat(errors)
        .isNotNull()
        .hasSize(1)
        .containsExactly(
            new AbstractMap.SimpleEntry<>(
                "Request.InvalidRequestId",
                new String[] {"The request id must match the route id."}));
  }

  @Test
  void
      givenBadRequestExceptionWithMultipleErrorsAndNoDuplicateCodes_whenGetErrors_thenReturnCorrectSizeAndErrors() {
    BadRequestException badRequestException =
        Mockito.mock(
            BadRequestException.class,
            Mockito.withSettings()
                .useConstructor(
                    "Bad request exception.",
                    new ErrorDetail[] {
                      new ErrorDetail(
                          "Request.InvalidRequestId", "The request id must match the route id."),
                      new ErrorDetail("Group.InvalidGroup", "The group was invalid."),
                      new ErrorDetail(
                          "Reservation.InvalidReservation", "The reservation was invalid.")
                    })
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    Map<String, String[]> errors = badRequestException.getErrors();

    assertThat(errors)
        .isNotNull()
        .hasSize(3)
        .containsExactlyInAnyOrderEntriesOf(
            Map.of(
                "Request.InvalidRequestId",
                new String[] {"The request id must match the route id."},
                "Group.InvalidGroup",
                new String[] {"The group was invalid."},
                "Reservation.InvalidReservation",
                new String[] {"The reservation was invalid."}));
  }

  @Test
  void
      givenBadRequestExceptionWithMultipleErrorsAndDuplicateCodes_whenGetErrors_thenReturnCorrectSizeAndErrors() {
    BadRequestException badRequestException =
        Mockito.mock(
            BadRequestException.class,
            Mockito.withSettings()
                .useConstructor(
                    "Bad request exception.",
                    new ErrorDetail[] {
                      new ErrorDetail(
                          "Request.InvalidRequestId", "The request id must match the route id."),
                      new ErrorDetail(
                          "Group.StartInterval", "The group start interval must not be null."),
                      new ErrorDetail(
                          "Group.StartInterval", "The group start interval must be positive.")
                    })
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    Map<String, String[]> errors = badRequestException.getErrors();

    assertThat(errors)
        .isNotNull()
        .hasSize(2)
        .containsExactlyInAnyOrderEntriesOf(
            Map.of(
                "Request.InvalidRequestId",
                new String[] {"The request id must match the route id."},
                "Group.StartInterval",
                new String[] {
                  "The group start interval must not be null.",
                  "The group start interval must be positive."
                }));
  }
}
