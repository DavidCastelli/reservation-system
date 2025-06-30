package com.github.davidcastelli.reservationsystem.group;

import java.math.BigDecimal;

/**
 * A group DTO to return to the client when requesting group information.
 *
 * @param groupId The group id.
 * @param minPeople The minimum number of people.
 * @param maxPeople The maximum number of people.
 * @param admissionPrice The admission price.
 * @param startInterval The start interval.
 */
record GroupDto(
    long groupId, int minPeople, int maxPeople, BigDecimal admissionPrice, int startInterval) {}
