package com.github.davidcastelli.reservationsystem.group;

import com.github.davidcastelli.reservationsystem.common.ErrorDetail;
import com.github.davidcastelli.reservationsystem.common.exception.InvalidRequestIdException;
import com.github.davidcastelli.reservationsystem.common.utility.RequestErrors;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/groups")
class GroupController implements GroupApi {

  private final GroupService groupService;
  private final GroupMapper groupMapper;

  /**
   * Creates a {@link GroupController GroupController}.
   *
   * @param groupService The group service.
   * @param groupMapper The group mapper.
   */
  GroupController(GroupService groupService, GroupMapper groupMapper) {
    this.groupService = groupService;
    this.groupMapper = groupMapper;
  }

  @GetMapping(value = "")
  @Override
  public List<GroupDto> findAll() {
    return groupService.findAll().stream().map(groupMapper::toDto).toList();
  }

  @GetMapping(value = "/{id}")
  @Override
  public GroupDto findById(@PathVariable long id) {
    var group = groupService.findById(id);
    return groupMapper.toDto(group);
  }

  @PostMapping(value = "")
  @Override
  public ResponseEntity<Void> create(@RequestBody @Valid GroupRequest request) {
    var group = groupMapper.toGroup(request);
    long groupId = groupService.create(group);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(groupId);

    return ResponseEntity.created(location).build();
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Override
  public void update(@PathVariable long id, @RequestBody @Valid GroupRequest request) {
    if (id != request.groupId()) {
      throw new InvalidRequestIdException(new ErrorDetail[] {RequestErrors.InvalidRequestId()});
    }

    var group = groupMapper.toGroup(request);
    groupService.update(id, group);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Override
  public void delete(@PathVariable long id) {
    groupService.delete(id);
  }
}
