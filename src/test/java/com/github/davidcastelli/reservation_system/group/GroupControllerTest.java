package com.github.davidcastelli.reservation_system.group;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davidcastelli.reservation_system.common.exception.GroupNotFoundException;
import com.github.davidcastelli.reservation_system.common.model.Group;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@NullUnmarked
@WebMvcTest(GroupController.class)
class GroupControllerTest {

  @Autowired private MockMvcTester mockMvcTester;

  @MockitoBean private GroupService groupService;

  @MockitoBean private GroupMapper groupMapper;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void givenUnacceptableMediaType_whenFindAll_thenReturnCorrectStatusContentTypeFailureMessage() {
    assertThat(mockMvcTester.get().uri("/api/groups").accept(MediaType.APPLICATION_XML))
        .hasFailed()
        .hasStatus(HttpStatus.NOT_ACCEPTABLE)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
        .failure()
        .hasMessage("No acceptable representation");
  }

  @Test
  void givenGroups_whenFindAll_thenReturnCorrectStatusContentTypeBody() {
    List<Group> groups =
        List.of(
            new Group(1L, 1, 5, new BigDecimal("13.99"), 4),
            new Group(2L, 6, 10, new BigDecimal("13.99"), 8),
            new Group(3L, 11, 15, new BigDecimal("13.99"), 12));
    when(groupService.findAll()).thenReturn(groups);
    when(groupMapper.toDto(any(Group.class)))
        .thenReturn(
            new GroupDto(1L, 1, 5, new BigDecimal("13.99"), 4),
            new GroupDto(2L, 6, 10, new BigDecimal("13.99"), 8),
            new GroupDto(3L, 11, 15, new BigDecimal("13.99"), 12));

    assertThat(mockMvcTester.get().uri("/api/groups").accept(MediaType.APPLICATION_JSON))
        .doesNotHaveFailed()
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON)
        .bodyJson()
        .convertTo(InstanceOfAssertFactories.list(GroupDto.class))
        .isNotNull()
        .hasSize(3)
        .containsExactly(
            new GroupDto(1L, 1, 5, new BigDecimal("13.99"), 4),
            new GroupDto(2L, 6, 10, new BigDecimal("13.99"), 8),
            new GroupDto(3L, 11, 15, new BigDecimal("13.99"), 12));
  }

  @Test
  void givenNoGroups_whenFindAll_thenReturnCorrectStatusContentTypeBody() {
    List<Group> groups = Collections.emptyList();
    when(groupService.findAll()).thenReturn(groups);

    assertThat(mockMvcTester.get().uri("/api/groups").accept(MediaType.APPLICATION_JSON))
        .doesNotHaveFailed()
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON)
        .bodyJson()
        .convertTo(InstanceOfAssertFactories.list(GroupDto.class))
        .isNotNull()
        .isEmpty();
  }

  @Test
  void givenNonExistingId_whenFindById_thenReturnCorrectStatusContentTypeFailureMessage() {
    when(groupService.findById(2L)).thenThrow(new GroupNotFoundException(2L));

    assertThat(mockMvcTester.get().uri("/api/groups/{id}", 2L).accept(MediaType.APPLICATION_JSON))
        .hasFailed()
        .hasStatus(HttpStatus.NOT_FOUND)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
        .failure()
        .hasMessage("Group with id: 2 could not be found.");
  }

  @Test
  void givenExistingId_whenFindById_thenReturnCorrectStatusContentTypeBody() {
    Group group = new Group(1L, 1, 5, new BigDecimal("13.99"), 4);
    when(groupService.findById(1L)).thenReturn(group);
    when(groupMapper.toDto(group)).thenReturn(new GroupDto(1L, 1, 5, new BigDecimal("13.99"), 4));

    assertThat(mockMvcTester.get().uri("/api/groups/{id}", 1L).accept(MediaType.APPLICATION_JSON))
        .doesNotHaveFailed()
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON)
        .bodyJson()
        .convertTo(GroupDto.class)
        .isNotNull()
        .hasOnlyFields("groupId", "minPeople", "maxPeople", "admissionPrice", "startInterval")
        .isEqualTo(new GroupDto(1L, 1, 5, new BigDecimal("13.99"), 4));
  }

  @Test
  void givenNullGroupRequest_whenCreate_thenReturnCorrectStatusContentType() throws Exception {
    assertThat(
            mockMvcTester
                .post()
                .uri("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
        .hasFailed()
        .hasStatus(HttpStatus.BAD_REQUEST)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
  }

  @Test
  void givenUnsupportedContentType_whenCreate_thenCorrectStatusContentType() {
    assertThat(mockMvcTester.post().uri("/api/groups").contentType(MediaType.APPLICATION_XML))
        .hasFailed()
        .hasStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
  }

  @Test
  void givenInvalidGroupRequest_whenCreate_thenReturnCorrectStatusContentType() throws Exception {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(0L, 1, 5, null, -4);

    assertThat(
            mockMvcTester
                .post()
                .uri("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
        .hasFailed()
        .hasStatus(HttpStatus.BAD_REQUEST)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
  }

  @Test
  void givenValidGroupRequest_whenCreate_thenReturnCorrectStatusLocationHeaderEmptyBody()
      throws Exception {
    GroupRequest groupRequest = new GroupRequest(0L, 1, 5, new BigDecimal("13.99"), 4);
    Group group = new Group(0L, 1, 5, new BigDecimal("13.99"), 4);
    when(groupMapper.toGroup(groupRequest)).thenReturn(group);
    when(groupService.create(group)).thenReturn(1L);

    assertThat(
            mockMvcTester
                .post()
                .uri("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
        .doesNotHaveFailed()
        .hasStatus(HttpStatus.CREATED)
        .doesNotContainHeader("Content-Type")
        .satisfies(
            mvcTestResult ->
                assertThat(mvcTestResult)
                    .headers()
                    .extracting(HttpHeaders::getLocation, InstanceOfAssertFactories.URI_TYPE)
                    .asString()
                    .contains("/api/groups/1"))
        .body()
        .isEmpty();
  }

  @Test
  void givenDifferentRouteAndRequestId_whenUpdate_thenReturnCorrectStatusContentTypeFailureMessage()
      throws Exception {
    GroupRequest groupRequest = new GroupRequest(2L, 1, 5, new BigDecimal("13.99"), 4);

    assertThat(
            mockMvcTester
                .put()
                .uri("/api/groups/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
        .hasFailed()
        .hasStatus(HttpStatus.BAD_REQUEST)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
        .failure()
        .hasMessage("Request validation failed.");
  }

  @Test
  void givenNonExistingGroupId_whenUpdate_thenReturnCorrectStatusContentTypeFailureMessage()
      throws Exception {
    GroupRequest groupRequest = new GroupRequest(2L, 1, 5, new BigDecimal("13.99"), 4);
    Group group = new Group(2L, 1, 5, new BigDecimal("13.99"), 4);
    when(groupMapper.toGroup(groupRequest)).thenReturn(group);
    doThrow(new GroupNotFoundException(2L)).when(groupService).update(2L, group);

    assertThat(
            mockMvcTester
                .put()
                .uri("/api/groups/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
        .hasFailed()
        .hasStatus(HttpStatus.NOT_FOUND)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
        .failure()
        .hasMessage("Group with id: 2 could not be found.");
  }

  @Test
  void givenInvalidGroupRequest_whenUpdate_thenReturnCorrectStatusContentType() throws Exception {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, null, -4);

    assertThat(
            mockMvcTester
                .put()
                .uri("/api/groups/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
        .hasFailed()
        .hasStatus(HttpStatus.BAD_REQUEST)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
  }

  @Test
  void givenValidGroupRequest_whenUpdate_thenReturnCorrectStatusEmptyBody() throws Exception {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4);
    Group group = new Group(1L, 1, 5, new BigDecimal("13.99"), 4);
    when(groupMapper.toGroup(groupRequest)).thenReturn(group);

    assertThat(
            mockMvcTester
                .put()
                .uri("/api/groups/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
        .doesNotHaveFailed()
        .hasStatus(HttpStatus.NO_CONTENT)
        .doesNotContainHeader("Content-Type")
        .body()
        .isEmpty();

    verify(groupService, times(1)).update(1L, group);
  }

  @Test
  void givenNonExistingId_whenDelete_thenReturnCorrectStatusContentTypeFailureMessage() {
    doThrow(new GroupNotFoundException(2L)).when(groupService).delete(2L);

    assertThat(mockMvcTester.delete().uri("/api/groups/{id}", 2L))
        .hasFailed()
        .hasStatus(HttpStatus.NOT_FOUND)
        .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
        .failure()
        .hasMessage("Group with id: 2 could not be found.");
  }

  @Test
  void givenExistingId_whenDelete_thenReturnCorrectStatusEmptyBody() {
    assertThat(mockMvcTester.delete().uri("/api/groups/{id}", 1L))
        .doesNotHaveFailed()
        .hasStatus(HttpStatus.NO_CONTENT)
        .doesNotContainHeader("Content-Type")
        .body()
        .isEmpty();

    verify(groupService, times(1)).delete(1L);
  }
}
