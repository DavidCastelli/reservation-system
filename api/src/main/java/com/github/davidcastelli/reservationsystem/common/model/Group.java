package com.github.davidcastelli.reservationsystem.common.model;

import java.math.BigDecimal;

/**
 * A record to hold group information used in making a reservation. Users are assigned to a group
 * based on if the number of people in the reservation falls in the range of min and max people.
 * Both min people and max people are inclusive. A user making a reservation can only ever be
 * assigned to one group. The group holds important information for making a reservation such as the
 * start interval which determines how much time is reserved for the group to start, and admission
 * price to determine how much each person pays within the group. Bigger group sizes may have a
 * reduced admission price and a greater start interval.
 *
 * @param groupId The group id.
 * @param minPeople The minimum number of people.
 * @param maxPeople The maximum number of people.
 * @param admissionPrice The admission price.
 * @param startInterval The start interval.
 */
public record Group(
    long groupId, int minPeople, int maxPeople, BigDecimal admissionPrice, int startInterval) {}
