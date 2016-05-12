package uk.co.solutions4j.network.device.health.health;

import uk.co.solutions4j.network.device.health.comms.Messenger;
import uk.co.solutions4j.network.device.health.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "network")
public class NetworkDeviceHealthChecker implements HealthIndicator {

    private static SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

    private List<Device> devices = new ArrayList<>();
    private Set<Device> offlineSet = new HashSet<>();

    @Autowired
    private Messenger messenger;

    public NetworkDeviceHealthChecker(){

    }


    @Override
    public Health health() {
        boolean healthy = true;
        for(Device device:devices) {
            if(device!=null && device.getURI()!=null) {
                if (!ping(device)) {
                    healthy = false;
                    System.out.println("got a ping failure on device " + device.getName());
                    sendFailureMessages(device, " Ping failed");
                } else if (!get(device)) {
                    healthy = false;
                    System.out.println("got a get failure on device " + device.getName());
                    sendFailureMessages(device, " HTTP Get failed");
                } else {
                    offlineSet.remove(device);
                }
            }
        }
        return healthy? Health.up().build() : Health.down().withDetail("Error Code", 1).build();
    }

    private void sendFailureMessages(Device device, String message) {
        if(!offlineSet.contains(device)) {
            messenger.sendMessage(device, message);
            offlineSet.add(device);
        }
    }

    private boolean ping(Device device) {
        try {
            InetAddress address = InetAddress.getByName(device.getURI().getHost());
            return address.isReachable(null, 0, 1000);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean get(Device device){
        try {
            //if we have a URL from the uri then try to make a get request
            device.getURI().toURL(); //ignore the output
            ClientHttpResponse response = factory.createRequest(device.getURI(), HttpMethod.GET).execute();
            if(response.getStatusCode().is4xxClientError())
                return false;
        } catch (IllegalArgumentException|IOException exception) {
            //doesn't matter, just means it wasn't a url so we can't do a get
        }
        return true;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }


}