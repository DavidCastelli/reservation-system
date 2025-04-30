package com.github.davidcastelli.reservation_system.group;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

@NullUnmarked
class GroupDtoTest {

  private JacksonTester<GroupDto> jacksonTester;

  private GroupDto groupDto;

  @BeforeEach
  void init() {
    ObjectMapper objectMapper = new ObjectMapper();
    JacksonTester.initFields(this, objectMapper);

    groupDto = new GroupDto(1L, 1, 5, new BigDecimal("13.99"), 4);
  }

  @Test
  void whenConstructingGroupDto_thenCorrectGroupDtoProperties() {
    assertThat(groupDto)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("groupId", "minPeople", "maxPeople", "admissionPrice", "startInterval")
        .returns(1L, from(GroupDto::groupId))
        .returns(1, from(GroupDto::minPeople))
        .returns(5, from(GroupDto::maxPeople))
        .returns(new BigDecimal("13.99"), from(GroupDto::admissionPrice))
        .returns(4, from(GroupDto::startInterval));
  }

  @Test
  void givenGroupDto_whenSerialized_thenReturnCorrectJson() throws Exception {
    String expected =
        """
              {
                "groupId": 1,
                "minPeople": 1,
                "maxPeople": 5,
                "admissionPrice": 13.99,
                "startInterval": 4
              }
            """;

    assertThat(jacksonTester.write(groupDto)).isNotNull().isEqualToJson(expected);
  }
}
