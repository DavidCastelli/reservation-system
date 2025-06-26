package com.github.davidcastelli.reservation_system.common.model;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

@NullUnmarked
class GroupTest {

  @Test
  void whenConstructingGroup_thenCorrectGroupProperties() {
    Group group = new Group(1L, 1, 5, new BigDecimal("13.99"), 4);

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
