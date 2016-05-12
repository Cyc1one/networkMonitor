package uk.co.solutions4j.network.device.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class NetworkHealthApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(NetworkHealthApplication.class, args);
	}
}
