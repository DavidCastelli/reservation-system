package com.github.davidcastelli.reservation_system.group;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/** Annotation used for group size validation. */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupSizeValidator.class)
@Documented
public @interface ValidGroupSize {
  String message() default
      "The maximum number of people should be greater than the minimum number of people";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
