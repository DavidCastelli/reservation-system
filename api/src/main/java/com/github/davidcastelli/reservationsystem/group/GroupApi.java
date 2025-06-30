package com.github.davidcastelli.reservationsystem.group;

import com.github.davidcastelli.reservationsystem.common.model.Group;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/** Group controller for handling requests for {@link Group Groups}. */
@Tag(name = "Groups", description = "Endpoints for performing operations related to groups.")
interface GroupApi {

  /**
   * Endpoint for finding all groups.
   *
   * @return A list of {@link GroupDto GroupDtos}.
   */
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {
              @Content(
                  array = @ArraySchema(schema = @Schema(implementation = GroupDto.class)),
                  mediaType = "application/json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                  [
                                    {
                                        "groupId": 1,
                                        "minPeople": 1,
                                        "maxPeople": 5,
                                        "admissionPrice": 13.99,
                                        "startInterval": 4
                                    },
                                    {
                                        "groupId": 2,
                                        "minPeople": 6,
                                        "maxPeople": 10,
                                        "admissionPrice": 13.99,
                                        "startInterval": 8
                                    },
                                    {
                                        "groupId": 3,
                                        "minPeople": 11,
                                        "maxPeople": 15,
                                        "admissionPrice": 11.99,
                                        "startInterval": 12
                                    }
                                  ]
                              """))
            })
      })
  List<GroupDto> findAll();

  /**
   * Endpoint for finding a group by id.
   *
   * @param id The id of the group to find.
   * @return A {@link GroupDto GroupDto}.
   */
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = {
              @Content(
                  schema = @Schema(type = "object", additionalPropertiesSchema = GroupDto.class),
                  mediaType = "application/json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                {
                                    "groupId": 1,
                                    "minPeople": 1,
                                    "maxPeople": 5,
                                    "admissionPrice": 13.99,
                                    "startInterval": 4
                                }
                                """))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Group not found",
            content = {
              @Content(
                  schema =
                      @Schema(type = "object", additionalPropertiesSchema = ProblemDetail.class),
                  mediaType = "application/problem+json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                {
                                    "type": "https://tools.ietf.org/html/rfc9110#section-15.5.5",
                                    "title": "Not Found",
                                    "status": 404,
                                    "detail": "Group with id: 2 could not be found.",
                                    "instance": "/api/groups/2"
                                }
                              """))
            })
      })
  GroupDto findById(long id);

  /**
   * Endpoint for updating a group.
   *
   * @param request The request to create a group.
   * @return A response entity.
   */
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            headers = {
              @Header(
                  name = "Location",
                  description =
                      "Location of the newly created resource as a URI with an absolute path",
                  required = true,
                  schema = @Schema(type = "string", format = "uri"))
            },
            description = "Group created"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  schema =
                      @Schema(type = "object", additionalPropertiesSchema = ProblemDetail.class),
                  mediaType = "application/problem+json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                {
                                    "type": "https://tools.ietf.org/html/rfc9110#section-15.5.1",
                                    "title": "Bad Request",
                                    "status": 400,
                                    "detail": "Request validation failed.",
                                    "instance": "/api/groups/15"
                                }
                              """))
            }),
      })
  ResponseEntity<Void> create(
      @RequestBody(
              description = "Group to create",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = GroupRequest.class),
                      examples =
                          @ExampleObject(
                              value =
                                  """
                                      {
                                        "groupId": 0,
                                        "minPeople": 1,
                                        "maxPeople": 5,
                                        "admissionPrice": 13.99,
                                        "startInterval": 4
                                      }
                                  """)))
          GroupRequest request);

  /**
   * Endpoint for updating a group.
   *
   * @param id The id of the group to update.
   * @param request The request to update the group.
   */
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Group updated"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  schema =
                      @Schema(type = "object", additionalPropertiesSchema = ProblemDetail.class),
                  mediaType = "application/problem+json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                    {
                                        "type": "https://tools.ietf.org/html/rfc9110#section-15.5.1",
                                        "title": "Bad Request",
                                        "status": 400,
                                        "detail": "Request validation failed.",
                                        "instance": "/api/groups/17"
                                    }
                                """))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Group not found",
            content = {
              @Content(
                  schema =
                      @Schema(type = "object", additionalPropertiesSchema = ProblemDetail.class),
                  mediaType = "application/problem+json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                    {
                                        "type": "https://tools.ietf.org/html/rfc9110#section-15.5.5",
                                        "title": "Not Found",
                                        "status": 404,
                                        "detail": "Group with id: 42 could not be found.",
                                        "instance": "/api/groups/42"
                                    }
                                """))
            })
      })
  void update(
      long id,
      @RequestBody(
              description = "Group to update",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = GroupRequest.class),
                      examples =
                          @ExampleObject(
                              value =
                                  """
                                      {
                                        "groupId": 1,
                                        "minPeople": 6,
                                        "maxPeople": 10,
                                        "admissionPrice": 13.99,
                                        "startInterval": 8
                                      }
                                  """)))
          GroupRequest request);

  /**
   * Endpoint for deleting a group.
   *
   * @param id The id of the group to delete.
   */
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Group deleted"),
        @ApiResponse(
            responseCode = "404",
            description = "Group not found",
            content = {
              @Content(
                  schema =
                      @Schema(type = "object", additionalPropertiesSchema = ProblemDetail.class),
                  mediaType = "application/problem+json",
                  examples =
                      @ExampleObject(
                          value =
                              """
                                    {
                                        "type": "https://tools.ietf.org/html/rfc9110#section-15.5.5",
                                        "title": "Not Found",
                                        "status": 404,
                                        "detail": "Group with id: 42 could not be found.",
                                        "instance": "/api/groups/42"
                                    }
                                 """))
            })
      })
  void delete(long id);
}
