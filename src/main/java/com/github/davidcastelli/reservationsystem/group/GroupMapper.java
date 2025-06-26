package com.github.davidcastelli.reservationsystem.group;

import com.github.davidcastelli.reservationsystem.common.model.Group;
import org.springframework.stereotype.Component;

/** Mapper used to convert a group request to a group and a group to a group DTO. */
@Component
class GroupMapper {

  /**
   * Maps a group to a group DTO.
   *
   * @param group The group to map.
   * @return A {@link GroupDto GroupDto}.
   */
  GroupDto toDto(Group group) {
    return new GroupDto(
        group.groupId(),
        group.minPeople(),
        group.maxPeople(),
        group.admissionPrice(),
        group.startInterval());
  }

  /**
   * Maps a group request to a group.
   *
   * @param dto The group request to map.
   * @return A {@link Group Group}.
   */
  Group toGroup(GroupRequest dto) {
    return new Group(
        dto.groupId(), dto.minPeople(), dto.maxPeople(), dto.admissionPrice(), dto.startInterval());
  }
}
