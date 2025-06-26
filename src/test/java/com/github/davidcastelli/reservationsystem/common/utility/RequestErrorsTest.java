package com.github.davidcastelli.reservationsystem.common.utility;

import static org.assertj.core.api.Assertions.*;

import com.github.davidcastelli.reservationsystem.common.ErrorDetail;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

@NullUnmarked
class RequestErrorsTest {

  @Test
  void whenRequestErrorsInvalidRequestId_thenReturnCorrectErrorDetail() {
    ErrorDetail errorDetail = RequestErrors.InvalidRequestId();

    assertThat(errorDetail)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("code", "description")
        .returns("Request.InvalidRequestId", from(ErrorDetail::code))
        .returns("The request id must match the route id.", from(ErrorDetail::description));
  }
}
