package com.github.davidcastelli.reservationsystem.common.exception;

import static org.assertj.core.api.Assertions.*;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@NullUnmarked
class NotFoundExceptionTest {

  @Test
  void givenNotFoundException_whenGetMessage_thenReturnCorrectMessage() {
    NotFoundException notFoundException =
        Mockito.mock(
            NotFoundException.class,
            Mockito.withSettings()
                .useConstructor("Not found exception.")
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    String message = notFoundException.getMessage();

    assertThat(message).isNotNull().isEqualTo("Not found exception.");
  }
}
