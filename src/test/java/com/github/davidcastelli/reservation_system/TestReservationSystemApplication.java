package com.github.davidcastelli.reservation_system;

import org.springframework.boot.SpringApplication;

public class TestReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReservationSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
