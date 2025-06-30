package com.github.davidcastelli.reservation_system.common.exception;

/** Exception which is thrown when a group could not be found. */
public class GroupNotFoundException extends NotFoundException {
  /**
   * Create a {@link GroupNotFoundException GroupNotFoundException}.
   *
   * @param id The id of the group which could not be found.
   */
  public GroupNotFoundException(long id) {
    super(String.format("Group with id: %d could not be found.", id));
  }
}
