package com.github.davidcastelli.reservationsystem.group;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

@NullUnmarked
class GroupRequestTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  private JacksonTester<GroupRequest> jacksonTester;

  @BeforeAll
  static void initAll() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @BeforeEach
  void init() {
    ObjectMapper objectMapper = new ObjectMapper();
    JacksonTester.initFields(this, objectMapper);
  }

  @Test
  void whenConstructingGroupRequest_thenCorrectGroupRequestProperties() {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4);

    assertThat(groupRequest)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("groupId", "minPeople", "maxPeople", "admissionPrice", "startInterval")
        .returns(1L, from(GroupRequest::groupId))
        .returns(1, from(GroupRequest::minPeople))
        .returns(5, from(GroupRequest::maxPeople))
        .returns(new BigDecimal("13.99"), from(GroupRequest::admissionPrice))
        .returns(4, from(GroupRequest::startInterval));
  }

  @Test
  void givenGroupRequestJson_whenDeserialize_thenReturnCorrectGroupRequest() throws Exception {
    String groupRequestJson =
        """
              {
                "groupId": 1,
                "minPeople": 1,
                "maxPeople": 5,
                "admissionPrice": 13.99,
                "startInterval": 4
              }
            """;

    assertThat(jacksonTester.parse(groupRequestJson))
        .isNotNull()
        .isEqualTo(new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4));
  }

  @Test
  void
      givenGroupRequestWithNegativeMinPeople_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(1L, -1, 5, new BigDecimal("13.99"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo("The minimum number of people must be greater than 0");
  }

  @Test
  void
      givenGroupRequestWithNegativeMaxPeople_whenValidatorValidate_thenReturnConstraintViolationsWithSizeTwoAndCorrectFilteredMessage() {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(1L, 1, -5, new BigDecimal("13.99"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    // Expected size is two because it is not possible to isolate this constraint violation.
    // Given a negative max people, max people will either be less than min people, or both min and
    // max people will be negative.
    assertThat(constraintViolations).hasSize(2);
    assertThat(constraintViolations)
        .filteredOn(
            (constraintViolation) ->
                constraintViolation
                    .getMessage()
                    .equals("The maximum number of people must be greater than 0"))
        .hasSize(1);
  }

  @Test
  void
      givenGroupRequestWithNullAdmissionPrice_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, null, 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo("The admission price must not be null");
  }

  @Test
  void
      givenGroupRequestWithNegativeAdmissionPrice_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("-13.99"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo("The admission price must be zero or greater");
  }

  @Test
  void
      givenGroupRequestWithAdmissionPriceWithScaleGreaterThanTwo_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.999"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo(
            "The admission price's numeric value is out of bounds (<10 digits>.<2 digits> expected)");
  }

  @Test
  void
      givenGroupRequestWithAdmissionPriceWithPrecisionGreaterThanTwelve_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13333333333.99"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo(
            "The admission price's numeric value is out of bounds (<10 digits>.<2 digits> expected)");
  }

  @Test
  void
      givenGroupRequestWithNegativeStartInterval_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    @SuppressWarnings("DataFlowIssue") // Creating invalid object for testing
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), -4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo("The start interval must be greater than 0");
  }

  @Test
  void
      givenGroupRequestWithMinPeopleGreaterThanMaxPeople_whenValidatorValidate_thenReturnConstraintViolationsWithSizeOneAndCorrectMessage() {
    GroupRequest groupRequest = new GroupRequest(1L, 5, 1, new BigDecimal("13.99"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).hasSize(1);
    assertThat(constraintViolations.iterator().next().getMessage())
        .isEqualTo(
            "The maximum number of people should be greater than the minimum number of people");
  }

  @Test
  void givenValidGroupRequest_whenValidatorValidate_thenReturnEmptyConstraintViolations() {
    GroupRequest groupRequest = new GroupRequest(1L, 1, 5, new BigDecimal("13.99"), 4);

    Set<ConstraintViolation<GroupRequest>> constraintViolations = validator.validate(groupRequest);

    assertThat(constraintViolations).isEmpty();
  }

  @AfterAll
  static void tearDownAll() {
    validatorFactory.close();
  }
}
