package com.github.davidcastelli.reservationsystem.group;

import static org.assertj.core.api.Assertions.*;

import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
class GroupSizeValidatorTest {

  @Mock private ConstraintValidatorContext constraintValidatorContext;

  @Test
  void givenNullGroupRequest_whenGroupSizeValidatorIsValid_returnTrue() {
    GroupSizeValidator groupSizeValidator = new GroupSizeValidator();

    boolean result = groupSizeValidator.isValid(null, constraintValidatorContext);

    assertThat(result).isTrue();
  }

  @Test
  void
      givenGroupRequestWithMinPeopleGreaterThanMaxPeople_whenGroupSizeValidatorIsValid_returnFalse() {
    GroupSizeValidator groupSizeValidator = new GroupSizeValidator();
    GroupRequest groupRequest = new GroupRequest(1L, 5, 1, new BigDecimal("13.99"), 4);

    boolean result = groupSizeValidator.isValid(groupRequest, constraintValidatorContext);

    assertThat(result).isFalse();
  }

  @Test
  void givenGroupRequestWithMinPeopleEqualToMaxPeople_whenGroupSizeValidatorIsValid_returnFalse() {
    GroupSizeValidator groupSizeValidator = new GroupSizeValidator();
    GroupRequest groupRequest = new GroupRequest(1L, 5, 5, new BigDecimal("13.99"), 4);

    boolean result = groupSizeValidator.isValid(groupRequest, constraintValidatorContext);

    assertThat(result).isFalse();
  }

  @Test
  void givenGroupRequestWithMinPeopleLessThanMaxPeople_whenGroupSizeValidatorIsValid_returnTrue() {
    GroupSizeValidator groupSizeValidator = new GroupSizeValidator();
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4);

    boolean result = groupSizeValidator.isValid(groupRequest, constraintValidatorContext);

    assertThat(result).isTrue();
  }
}
