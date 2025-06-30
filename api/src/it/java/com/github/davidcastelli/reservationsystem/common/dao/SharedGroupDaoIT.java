package com.github.davidcastelli.reservationsystem.common.dao;

import static org.assertj.core.api.Assertions.*;

import com.github.davidcastelli.reservationsystem.common.model.Group;
import java.math.BigDecimal;
import java.util.Optional;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@NullUnmarked
@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/db/testdata/init_groups.sql")
class SharedGroupDaoIT {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Autowired private SharedGroupDao sharedGroupDao;

  @TestConfiguration
  static class TestConfig {

    @Autowired private JdbcClient jdbcClient;

    @Bean
    public SharedGroupDao sharedGroupDao() {
      return new SharedGroupDao(jdbcClient);
    }
  }

  @Test
  void givenGroups_whenFindByPeople_thenReturnCorrectGroupWherePeopleIsBetweenMinAndMax() {
    int people = 2;

    Optional<Group> group = sharedGroupDao.findByPeople(people);

    assertThat(group)
        .isNotNull()
        .isNotEmpty()
        .get()
        .isEqualTo(new Group(1L, 1, 5, new BigDecimal("13.99"), 4));
  }

  @Test
  void givenGroupsWithNoneMatchingPeople_whenFindByPeople_thenReturnEmptyOptional() {
    int people = 20;

    Optional<Group> group = sharedGroupDao.findByPeople(people);

    assertThat(group).isNotNull().isEmpty();
  }
}
