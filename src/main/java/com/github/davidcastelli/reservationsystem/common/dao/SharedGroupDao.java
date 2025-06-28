package com.github.davidcastelli.reservationsystem.common.dao;

import com.github.davidcastelli.reservationsystem.common.model.Group;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

/** DAO used to perform common persistence operations on a {@link Group Group}. */
@Repository()
public class SharedGroupDao {

  private final JdbcClient jdbcClient;

  /**
   * Creates a {@link SharedGroupDao SharedGroupDao}.
   *
   * @param jdbcClient The jdbcClient.
   */
  SharedGroupDao(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  // The DB stores groups under a package table with a primary key of package_id
  // in order to avoid naming conflicts with the group keyword.
  // The row mapper is used to map package_id to groupId.
  RowMapper<Group> rowMapper =
      (rs, rowNum) ->
          new Group(
              rs.getInt("package_id"),
              rs.getInt("min_people"),
              rs.getInt("max_people"),
              rs.getBigDecimal("admission_price"),
              rs.getInt("start_interval"));

  /**
   * Performs the operation to retrieve a group based on the number of people.
   *
   * @param people The number of people in the group.
   * @return An optional {@link Group Group} containing a group if one was found with people between
   *     the groups min and max or empty otherwise.
   */
  public Optional<Group> findByPeople(long people) {
    return jdbcClient
        .sql(
            "SELECT package_id, min_people, max_people, admission_price, start_interval FROM package_v WHERE min_people <= :people AND max_people >= :people")
        .param("people", people)
        .query(rowMapper)
        .optional();
  }
}
