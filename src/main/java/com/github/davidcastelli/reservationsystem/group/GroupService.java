package com.github.davidcastelli.reservationsystem.group;

import com.github.davidcastelli.reservationsystem.common.exception.GroupNotFoundException;
import com.github.davidcastelli.reservationsystem.common.model.Group;
import java.util.List;
import org.springframework.stereotype.Service;

/** Service which provides methods for working with a {@link Group Group}. */
@Service
class GroupService {

  private final GroupDao groupDao;

  /**
   * Creates a {@link GroupService GroupService}.
   *
   * @param groupDao The group DAO.
   */
  GroupService(GroupDao groupDao) {
    this.groupDao = groupDao;
  }

  /**
   * Retrieves a list of all groups.
   *
   * @return The list of {@link Group Groups}.
   */
  List<Group> findAll() {
    return groupDao.findAll();
  }

  /**
   * Retrieves a group by id if it could be found.
   *
   * @param id The id of the group to retrieve.
   * @return A {@link Group Group}.
   * @throws GroupNotFoundException if no group could be found.
   */
  Group findById(long id) {
    return groupDao.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
  }

  /**
   * Creates a {@link Group Group}.
   *
   * @param group The group to create.
   * @return The id of the group which was created.
   */
  long create(Group group) {
    return groupDao.create(group);
  }

  /**
   * Updates a {@link Group Group}.
   *
   * @param id The id of the group to update.
   * @param group The new group used to replace the old group.
   * @throws GroupNotFoundException if the group to update could not be found.
   */
  void update(long id, Group group) {
    var oldGroup = groupDao.findById(id);
    if (oldGroup.isEmpty()) {
      throw new GroupNotFoundException(id);
    }
    groupDao.update(id, group);
  }

  /**
   * Deletes a {@link Group Group}.
   *
   * @param id The id of the group to delete.
   * @throws GroupNotFoundException if the group to delete could not be found.
   */
  void delete(long id) {
    var group = groupDao.findById(id);
    if (group.isEmpty()) {
      throw new GroupNotFoundException(id);
    }
    groupDao.delete(id);
  }
}
