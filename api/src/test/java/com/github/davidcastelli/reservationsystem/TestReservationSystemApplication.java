package com.github.davidcastelli.reservationsystem;

import org.springframework.boot.SpringApplication;

public class TestReservationSystemApplication {

  public static void main(String[] args) {
    SpringApplication.from(ReservationSystemApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
