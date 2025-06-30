package com.github.davidcastelli.reservation_system.common.exception;

import static org.assertj.core.api.Assertions.*;

import com.github.davidcastelli.reservation_system.common.ErrorDetail;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

@NullUnmarked
class InvalidRequestIdExceptionTest {

  @Test
  void givenInvalidRequestIdException_whenGetMessage_thenReturnCorrectMessage() {
    InvalidRequestIdException invalidRequestIdException =
        new InvalidRequestIdException(new ErrorDetail[] {});

    String message = invalidRequestIdException.getMessage();

    assertThat(message).isNotNull().isEqualTo("Request validation failed.");
  }
}
