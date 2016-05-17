package uk.co.solutions4j.network.device.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetworkHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetworkHealthApplication.class, args);
	}
}
