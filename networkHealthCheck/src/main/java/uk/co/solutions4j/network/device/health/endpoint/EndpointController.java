package uk.co.solutions4j.network.device.health.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}