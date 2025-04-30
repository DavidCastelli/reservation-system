package com.github.davidcastelli.reservation_system.group;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jspecify.annotations.Nullable;

/** Validator used to validate group size. */
public class GroupSizeValidator implements ConstraintValidator<ValidGroupSize, GroupRequest> {
  /** {@inheritDoc} */
  @Override
  public void initialize(ValidGroupSize constraintAnnotation) {}

  /**
   * {@inheritDoc} Checks if the group size is valid. A group size is valid as long as the maximum
   * number of people is strictly greater than the minimum number of people. A null request passes
   * validation.
   *
   * @param groupRequest The group request.
   * @param constraintValidatorContext The constraint validator context.
   * @return True if valid, false otherwise.
   */
  @Override
  public boolean isValid(
      @Nullable GroupRequest groupRequest, ConstraintValidatorContext constraintValidatorContext) {
    if (groupRequest == null) {
      return true;
    }

    return groupRequest.maxPeople() > groupRequest.minPeople();
  }
}
