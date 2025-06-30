package com.github.davidcastelli.reservation_system.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * A group request sent by the reservation manager to create or update a group.
 *
 * @param groupId The group id.
 * @param minPeople The minimum number of people.
 * @param maxPeople The maximum number of people.
 * @param admissionPrice The admission price.
 * @param startInterval The start interval.
 */
@ValidGroupSize
public record GroupRequest( // Has to be public because it is exposed by GroupSizeValidator
    long groupId,
    @Schema() @Positive(message = "The minimum number of people must be greater than 0")
        int minPeople,
    @Positive(message = "The maximum number of people must be greater than 0") int maxPeople,
    @NotNull(message = "The admission price must not be null")
        @PositiveOrZero(message = "The admission price must be zero or greater")
        @Digits(
            integer = 10,
            fraction = 2,
            message =
                "The admission price's numeric value is out of bounds (<10 digits>.<2 digits> expected)")
        BigDecimal admissionPrice,
    @Positive(message = "The start interval must be greater than 0") int startInterval) {}
