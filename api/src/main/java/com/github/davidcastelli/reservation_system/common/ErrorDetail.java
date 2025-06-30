package com.github.davidcastelli.reservation_system.common;

/**
 * A record to hold information used to describe what went wrong with a request.
 *
 * @param code The error code.
 * @param description The error description.
 */
public record ErrorDetail(String code, String description) {}
