package com.github.davidcastelli.reservationsystem.group;

import static org.assertj.core.api.Assertions.*;

import com.github.davidcastelli.reservationsystem.common.model.Group;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@NullUnmarked
@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupDaoIT {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Autowired private GroupDao groupDao;

  @TestConfiguration
  static class TestConfig {

    @Autowired private JdbcClient jdbcClient;

    @Bean
    public GroupDao groupDao() {
      return new GroupDao(jdbcClient);
    }
  }

  @Test
  void givenGroups_whenFindAll_thenReturnCorrectGroups() {
    List<Group> initGroups =
        List.of(
            new Group(0L, 1, 5, new BigDecimal("13.99"), 4),
            new Group(0L, 6, 10, new BigDecimal("13.99"), 8),
            new Group(0L, 11, 15, new BigDecimal("11.99"), 12));
    for (Group group : initGroups) {
      groupDao.create(group);
    }

    List<Group> groups = groupDao.findAll();

    assertThat(groups).isNotNull().hasSize(3);
  }

  @Test
  void givenGroup_whenFindById_thenReturnCorrectGroup() {
    Group initGroup = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    long groupId = groupDao.create(initGroup);

    Optional<Group> group = groupDao.findById(groupId);

    assertThat(group)
        .isNotNull()
        .isNotEmpty()
        .get()
        .isEqualTo(new Group(groupId, 1, 5, new BigDecimal("13.99"), 4));
  }

  @Test
  void givenGroup_whenUpdate_thenReturnCorrectGroup() {
    Group group = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    long groupId = groupDao.create(group);
    Group updatedGroup = new Group(groupId, 1, 5, new BigDecimal("11.99"), 4);

    groupDao.update(groupId, updatedGroup);

    assertThat(groupDao.findById(groupId))
        .isNotNull()
        .isNotEmpty()
        .get()
        .isEqualTo(new Group(groupId, 1, 5, new BigDecimal("11.99"), 4));
  }

  @Test
  void givenGroups_whenDelete_returnCorrectGroups() {
    List<Group> initGroups =
        List.of(
            new Group(0L, 1, 5, new BigDecimal("13.99"), 4),
            new Group(0L, 6, 10, new BigDecimal("13.99"), 8),
            new Group(0L, 11, 15, new BigDecimal("11.99"), 12));
    for (Group group : initGroups) {
      groupDao.create(group);
    }
    long groupId = groupDao.create(new Group(0L, 16, 20, new BigDecimal("11.99"), 16));

    groupDao.delete(groupId);

    assertThat(groupDao.findAll())
        .isNotNull()
        .hasSize(3)
        .filteredOn(group -> group.groupId() == groupId)
        .isEmpty();
  }

  @Test
  void givenGroupWithNegativeMinPeople_whenCreate_thenThrowDataAccessException() {
    Group group = new Group(0L, -1, 5, new BigDecimal("13.99"), 4);

    Throwable thrown = catchThrowable(() -> groupDao.create(group));

    assertThat(thrown).isNotNull().isInstanceOf(DataAccessException.class);
  }

  @Test
  void givenGroupWithNegativeMaxPeople_whenCreate_thenThrowDataAccessException() {
    Group group = new Group(0L, 1, -5, new BigDecimal("13.99"), 4);

    Throwable thrown = catchThrowable(() -> groupDao.create(group));

    assertThat(thrown).isNotNull().isInstanceOf(DataAccessException.class);
  }

  @Test
  void givenGroupWithNegativeAdmissionPrice_whenCreate_thenThrowDataAccessException() {
    Group group = new Group(0L, 1, 5, new BigDecimal("-13.99"), 4);

    Throwable thrown = catchThrowable(() -> groupDao.create(group));

    assertThat(thrown).isNotNull().isInstanceOf(DataAccessException.class);
  }

  @Test
  void givenGroupWithNegativeStartInterval_whenCreate_thenThrowDataAccessException() {
    Group group = new Group(0L, 1, 5, new BigDecimal("13.99"), -4);

    Throwable thrown = catchThrowable(() -> groupDao.create(group));

    assertThat(thrown).isNotNull().isInstanceOf(DataAccessException.class);
  }

  @Test
  void givenGroupWithMinPeopleGreaterThanMaxPeople_whenCreate_thenThrowDataAccessException() {
    Group group = new Group(0L, 5, 1, new BigDecimal("13.99"), 4);

    Throwable thrown = catchThrowable(() -> groupDao.create(group));

    assertThat(thrown).isNotNull().isInstanceOf(DataAccessException.class);
  }

  @Test
  void givenTwoGroupsWithOverlappingMinMaxPeople_whenCreate_thenThrowDataAccessException() {
    Group group1 = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    Group group2 = new Group(0L, 3, 7, new BigDecimal("13.99"), 4);
    groupDao.create(group1);

    Throwable thrown = catchThrowable(() -> groupDao.create(group2));

    assertThat(thrown).isNotNull().isInstanceOf(DataAccessException.class);
  }
}
