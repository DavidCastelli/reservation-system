package com.github.davidcastelli.reservationsystem.group;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import com.github.davidcastelli.reservationsystem.common.model.Group;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@NullUnmarked
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GroupControllerIT {

  @LocalServerPort private Integer port;

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Autowired private GroupDao groupDao;

  @Autowired private JdbcClient jdbcClient;

  @BeforeEach
  void init() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    RestAssured.basePath = "/api";

    jdbcClient.sql("TRUNCATE TABLE package").update();
  }

  @Test
  void givenGroups_whenFindAll_thenReturnCorrectStatusCodeContentTypeBody() {
    List<Group> initGroups =
        List.of(
            new Group(0L, 1, 5, new BigDecimal("13.99"), 4),
            new Group(0L, 6, 10, new BigDecimal("13.99"), 8),
            new Group(0L, 11, 15, new BigDecimal("11.99"), 12));
    for (Group group : initGroups) {
      groupDao.create(group);
    }

    List<Group> groups =
        given()
            .accept(ContentType.JSON)
            .when()
            .get("/groups")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", Group.class);

    assertThat(groups)
        .isNotNull()
        .hasSize(3)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("groupId")
        .containsExactly(
            new Group(0L, 1, 5, new BigDecimal("13.99"), 4),
            new Group(0L, 6, 10, new BigDecimal("13.99"), 8),
            new Group(0L, 11, 15, new BigDecimal("11.99"), 12));
  }

  @Test
  void givenNoGroups_whenFindAll_thenReturnCorrectStatusCodeContentTypeEmptyBody() {
    List<Group> groups =
        given()
            .accept(ContentType.JSON)
            .when()
            .get("/groups")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", Group.class);

    assertThat(groups).isNotNull().isEmpty();
  }

  @Test
  void givenGroupWithExistingId_whenFindById_thenReturnCorrectStatusCodeContentTypeBody() {
    Group initGroup = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    long groupId = groupDao.create(initGroup);

    Group group =
        given()
            .accept(ContentType.JSON)
            .when()
            .get("/groups/{id}", groupId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .body()
            .as(Group.class);

    assertThat(group).isNotNull().isEqualTo(new Group(groupId, 1, 5, new BigDecimal("13.99"), 4));
  }

  @Test
  void givenGroupWithNonExistingId_whenFindById_thenReturnCorrectStatusCodeContentTypeFailure() {
    given()
        .accept(ContentType.JSON)
        .when()
        .get("/groups/{id}", 1L)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .contentType(ContentType.JSON);
  }

  @Test
  void givenInvalidGroup_whenCreate_thenReturnCorrectStatusCodeContentTypeFailure() {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(0L, 1, -5, new BigDecimal("13.99"), -4);

    given()
        .contentType(ContentType.JSON)
        .body(groupRequest)
        .when()
        .post("/groups")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON);
  }

  @Test
  void givenValidGroup_whenCreate_thenReturnCorrectStatusCodeLocationHeaderEmptyBody() {
    GroupRequest groupRequest = new GroupRequest(0L, 1, 5, new BigDecimal("13.99"), 4);

    byte[] response =
        given()
            .contentType(ContentType.JSON)
            .body(groupRequest)
            .when()
            .post("/groups")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", containsString("/api/groups/"))
            .extract()
            .body()
            .asByteArray();

    assertThat(response).isEmpty();
  }

  @Test
  void givenDifferentRouteAndRequestId_whenUpdate_thenReturnCorrectStatusCodeContentTypeFailure() {
    GroupRequest groupRequest = new GroupRequest(2L, 1, 5, new BigDecimal("13.99"), 4);

    given()
        .contentType(ContentType.JSON)
        .body(groupRequest)
        .when()
        .put("/groups/{id}", 1L)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON);
  }

  @Test
  void givenNonExistingGroupId_whenUpdate_thenReturnCorrectStatusCodeContentTypeFailure() {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4);

    given()
        .contentType(ContentType.JSON)
        .body(groupRequest)
        .when()
        .put("/groups/{id}", 1L)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .contentType(ContentType.JSON);
  }

  @Test
  void givenInvalidGroupRequest_whenUpdate_thenReturnCorrectStatusCodeContentTypeFailure() {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(1L, 1, -5, new BigDecimal("13.99"), -4);

    given()
        .contentType(ContentType.JSON)
        .body(groupRequest)
        .when()
        .put("/groups/{id}", 1L)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON);
  }

  @Test
  void givenValidGroupRequest_whenUpdate_thenReturnCorrectStatusCodeEmptyBody() {
    Group group = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    long groupId = groupDao.create(group);
    GroupRequest groupRequest = new GroupRequest(groupId, 1, 5, new BigDecimal("13.99"), 4);

    byte[] response =
        given()
            .contentType(ContentType.JSON)
            .body(groupRequest)
            .when()
            .put("/groups/{id}", groupId)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract()
            .body()
            .asByteArray();

    assertThat(response).isEmpty();
  }

  @Test
  void givenNonExistingGroupId_whenDelete_thenReturnCorrectStatusCodeContentTypeFailure() {
    when()
        .delete("/groups/{id}", 1L)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .contentType(ContentType.JSON);
  }

  @Test
  void givenExistingGroupId_whenDelete_thenReturnCorrectStatusCodeEmptyBody() {
    Group group = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    long groupId = groupDao.create(group);

    byte[] response =
        when()
            .delete("/groups/{id}", groupId)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract()
            .body()
            .asByteArray();

    assertThat(response).isEmpty();
  }
}
