package com.github.davidcastelli.reservation_system.group;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.davidcastelli.reservation_system.common.exception.GroupNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

  @Mock private GroupDao groupDao;

  @InjectMocks private GroupService groupService;

  private Group group;

  @BeforeEach
  void init() {
    group = new Group(1L, 1, 5, new BigDecimal("13.99"), 4);
  }

  @Test
  void whenFindAll_thenReturnCorrectGroupsSize() {
    Group group1 = new Group(2L, 6, 10, new BigDecimal("13.99"), 8);
    Group group2 = new Group(3L, 11, 15, new BigDecimal("13.99"), 12);

    when(groupDao.findAll()).thenReturn(List.of(group, group1, group2));

    List<Group> groups = groupService.findAll();

    assertThat(groups).isNotNull().hasSize(3);
  }

  @Test
  void givenExistingId_whenFindById_thenReturnCorrectGroup() {
    when(groupDao.findById(1L)).thenReturn(Optional.ofNullable(group));

    Group actual = groupService.findById(1L);

    assertThat(actual).isNotNull();
  }

  @Test
  void givenNonExistingId_whenFindById_thenThrowGroupNotFoundException() {
    when(groupDao.findById(2L)).thenReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> groupService.findById(2L));

    assertThat(thrown)
        .isNotNull()
        .isInstanceOf(GroupNotFoundException.class)
        .hasMessage("Group with id: 2 could not be found.");
  }

  @Test
  void givenGroup_whenCreate_thenReturnCorrectGroupId() {
    Group groupToCreate = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    when(groupDao.create(groupToCreate)).thenReturn(1L);

    long groupId = groupService.create(groupToCreate);

    assertThat(groupId).isEqualTo(1L);
  }

  @Test
  void givenGroupWithExistingId_whenUpdate_thenVerified() {
    when(groupDao.findById(1L)).thenReturn(Optional.ofNullable(group));

    groupService.update(1L, group);

    verify(groupDao, times(1)).update(1L, group);
  }

  @Test
  void givenGroupWithNonExistingId_whenUpdate_thenThrowGroupNotFoundException() {
    when(groupDao.findById(2L)).thenReturn(Optional.empty());
    Group newGroup = new Group(2L, 1, 5, new BigDecimal("13.99"), 8);

    Throwable thrown = catchThrowable(() -> groupService.update(2L, newGroup));

    assertThat(thrown)
        .isNotNull()
        .isInstanceOf(GroupNotFoundException.class)
        .hasMessage("Group with id: 2 could not be found.");
  }

  @Test
  void givenExistingId_whenDelete_thenVerified() {
    when(groupDao.findById(1L)).thenReturn(Optional.ofNullable(group));

    groupService.delete(1L);

    verify(groupDao, times(1)).delete(1L);
  }

  @Test
  void givenNonExistingId_whenDelete_thenThrowGroupNotFoundException() {
    when(groupDao.findById(2L)).thenReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> groupService.delete(2L));

    assertThat(thrown)
        .isNotNull()
        .isInstanceOf(GroupNotFoundException.class)
        .hasMessage("Group with id: 2 could not be found.");
  }
}
