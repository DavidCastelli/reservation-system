package com.github.davidcastelli.reservationsystem.common.exception;

import static org.assertj.core.api.Assertions.*;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

@NullUnmarked
class GroupNotFoundExceptionTest {

  @Test
  void givenGroupNotFoundException_whenGetMessage_thenReturnCorrectMessage() {
    GroupNotFoundException groupNotFoundException = new GroupNotFoundException(1);

    String message = groupNotFoundException.getMessage();

    assertThat(message).isNotNull().isEqualTo("Group with id: 1 could not be found.");
  }
}
