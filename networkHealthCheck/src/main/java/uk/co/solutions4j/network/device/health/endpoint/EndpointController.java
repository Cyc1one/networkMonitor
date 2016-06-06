package uk.co.solutions4j.network.device.health.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.solutions4j.network.device.health.health.NetworkDeviceHealthChecker;
import uk.co.solutions4j.network.device.health.model.Device;

import java.util.Set;

@RestController
public class EndpointController {

    @Autowired
    private NetworkDeviceHealthChecker healthChecker;


    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/healthService", produces = "application/json", method=RequestMethod.GET)
    public @ResponseBody Set<Device> healthService(){
        return healthChecker.getCurrentHealthList();
    }

}