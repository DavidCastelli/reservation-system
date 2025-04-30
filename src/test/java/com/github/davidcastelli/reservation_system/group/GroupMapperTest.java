package com.github.davidcastelli.reservation_system.group;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

@NullUnmarked
class GroupMapperTest {

  @Test
  void givenGroup_whenGroupMapperToDto_thenReturnGroupDtoWithCorrectProperties() {
    GroupMapper groupMapper = new GroupMapper();
    Group group = new Group(1L, 1, 5, new BigDecimal("13.99"), 4);

    GroupDto groupDto = groupMapper.toDto(group);

    assertThat(groupDto)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("groupId", "minPeople", "maxPeople", "admissionPrice", "startInterval")
        .returns(1L, from(GroupDto::groupId))
        .returns(1, from(GroupDto::minPeople))
        .returns(5, from(GroupDto::maxPeople))
        .returns(new BigDecimal("13.99"), from(GroupDto::admissionPrice))
        .returns(4, from(GroupDto::startInterval));
  }

  @Test
  void givenGroupRequest_whenGroupMapperToGroup_thenReturnGroupWithCorrectProperties() {
    GroupMapper groupMapper = new GroupMapper();
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4);

    Group group = groupMapper.toGroup(groupRequest);

    assertThat(group)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("groupId", "minPeople", "maxPeople", "admissionPrice", "startInterval")
        .returns(1L, from(Group::groupId))
        .returns(1, from(Group::minPeople))
        .returns(5, from(Group::maxPeople))
        .returns(new BigDecimal("13.99"), from(Group::admissionPrice))
        .returns(4, from(Group::startInterval));
  }
}
