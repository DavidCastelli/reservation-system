package com.github.davidcastelli.reservationsystem.common;

import static org.assertj.core.api.Assertions.*;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

@NullUnmarked
class ErrorDetailTest {

  @Test
  void whenConstructingErrorDetail_thenCorrectErrorDetailProperties() {
    ErrorDetail errorDetail =
        new ErrorDetail("Request.InvalidGroupId", "The request id must match the route id.");

    assertThat(errorDetail)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("code", "description")
        .returns("Request.InvalidGroupId", from(ErrorDetail::code))
        .returns("The request id must match the route id.", from(ErrorDetail::description));
  }
}
