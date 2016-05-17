package uk.co.solutions4j.network.device.health.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.co.solutions4j.network.device.health.health.NetworkDeviceHealthChecker;

@Component
public class ScheduledTasks {

    @Autowired
    private NetworkDeviceHealthChecker healthChecker;

    @Scheduled(fixedRateString = "${check.interval}")
    public void executeHealthChecks() {
        healthChecker.health();
    }
}
