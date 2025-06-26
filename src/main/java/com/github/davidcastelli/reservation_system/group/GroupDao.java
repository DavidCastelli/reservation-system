package com.github.davidcastelli.reservation_system.group;

import com.github.davidcastelli.reservation_system.common.model.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/** DAO used to perform persistence operations on a {@link Group Group}. */
@Repository
class GroupDao {

  private final JdbcClient jdbcClient;

  /**
   * Creates a {@link GroupDao GroupDao}.
   *
   * @param jdbcClient The jdbcClient.
   */
  GroupDao(JdbcClient jdbcClient) {
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
   * Performs the operation to retrieve all groups in the database.
   *
   * @return A list of {@link Group groups}.
   */
  List<Group> findAll() {
    return jdbcClient
        .sql(
            "SELECT package_id, min_people, max_people, admission_price, start_interval FROM package_v")
        .query(rowMapper)
        .list();
  }

  /**
   * Performs the operation to retrieve a group by id from the database.
   *
   * @param id The id of the group to find.
   * @return An optional {@link Group Group} containing a group if one was found with the given id
   *     or empty otherwise.
   */
  Optional<Group> findById(long id) {
    return jdbcClient
        .sql(
            "SELECT package_id, min_people, max_people, admission_price, start_interval FROM package_v WHERE package_id = :id")
        .param("id", id)
        .query(rowMapper)
        .optional();
  }

  /**
   * Performs the operation to create a group in the database.
   *
   * @param group The group to create.
   * @return The id of the group which was persisted.
   * @throws IllegalStateException if not exactly one group was created.
   * @throws IllegalArgumentException if the generated key is null.
   */
  long create(Group group) {
    var keyHolder = new GeneratedKeyHolder();
    int updated =
        jdbcClient
            .sql(
                "INSERT INTO package (min_people, max_people, admission_price, start_interval) VALUES (?, ?, ?, ?) RETURNING package_id")
            .params(
                group.minPeople(), group.maxPeople(), group.admissionPrice(), group.startInterval())
            .update(keyHolder);

    Assert.state(updated == 1, "Failed to create group");

    var key = keyHolder.getKey();
    Assert.notNull(key, "Failed to retrieve generated key during group insertion, key is null");

    return key.longValue();
  }

  /**
   * Performs the operation to update a group in the database.
   *
   * @param id The id of the group to update.
   * @param group The new group used to replace the old group.
   * @throws IllegalStateException if not exactly one group was updated.
   */
  void update(long id, Group group) {
    int updated =
        jdbcClient
            .sql(
                "UPDATE package SET min_people = ?, max_people = ?, admission_price = ?, start_interval = ? WHERE package_id = ?")
            .params(
                group.minPeople(),
                group.maxPeople(),
                group.admissionPrice(),
                group.startInterval(),
                id)
            .update();

    Assert.state(updated == 1, "Failed to update group with id: " + id);
  }

  /**
   * Performs the operation to delete a group in the database.
   *
   * @param id The id of the group to delete.
   * @throws IllegalStateException if not exactly one group was deleted.
   */
  void delete(long id) {
    int updated =
        jdbcClient.sql("DELETE FROM package WHERE package_id = :id").param("id", id).update();

    Assert.state(updated == 1, "Failed to delete group with id: " + id);
  }
}
