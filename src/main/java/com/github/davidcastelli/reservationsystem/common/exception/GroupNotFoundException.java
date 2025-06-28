package com.github.davidcastelli.reservationsystem.common.exception;

/** Exception which is thrown when a group could not be found. */
public class GroupNotFoundException extends NotFoundException {
  /**
   * Creates a {@link GroupNotFoundException GroupNotFoundException}.
   *
   * @param id The id of the group which could not be found.
   */
  public GroupNotFoundException(long id) {
    super(String.format("Group with id: %d could not be found.", id));
  }

  /**
   * Creates a {@link GroupNotFoundException GroupNotFoundException}.
   *
   * @param people The number of people in the group.
   */
  public GroupNotFoundException(int people) {
    super(String.format("There are no matching groups with %d people.", people));
  }
}
