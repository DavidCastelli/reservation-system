package com.github.davidcastelli.reservation_system;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi(
      @Value("${application-description}") String appDescription,
      @Value("${application-version}") String appVersion) {
    return new OpenAPI()
        .info(
            new Info()
                .title("Reservation System API")
                .version(appVersion)
                .description(appDescription));
  }
}
