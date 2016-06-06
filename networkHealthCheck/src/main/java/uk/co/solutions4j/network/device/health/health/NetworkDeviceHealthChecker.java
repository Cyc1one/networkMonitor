package uk.co.solutions4j.network.device.health.health;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import uk.co.solutions4j.network.device.health.comms.Messenger;
import uk.co.solutions4j.network.device.health.model.Device;

@Component
@ConfigurationProperties(prefix = "network")
public class NetworkDeviceHealthChecker implements HealthIndicator {

    private static SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

    private List<Device> devices = new ArrayList<>();
    private Set<Device> previouslyDetectedOffline = new HashSet<>();

    @Autowired
    private Messenger messenger;

    @Override
    public Health health() {
        boolean healthy = checkDevices();
        messageOfflineGroup(devices);
        return healthy? Health.up().build() : Health.down().withDetail("Error Code", 1).build();
    }

    private boolean checkDevices() {
        boolean healthy = true;
        for(Device device:devices) {
            device.setOffline(false);
            if(device!=null && device.getURI()!=null) {
                if (!ping(device)) {
                    healthy = false;
                    //System.out.println("got a ping failure on device " + device.getName());
                    device.setFailureMessage(" Ping");
                    device.setOffline(true);
                } else if (!get(device)) {
                    healthy = false;
                    //System.out.println("got a get failure on device " + device.getName());
                    device.setFailureMessage(" GET");
                    device.setOffline(true);
                } else {
                    previouslyDetectedOffline.remove(device);
                }
            }
        }
        return healthy;
    }

    public Set<Device> getCurrentHealthList(){
        checkDevices();
        return new HashSet<>(devices);
    }

    private void messageOfflineGroup(List<Device> offlineGroup) {
        List<Device> messageList = new ArrayList<>();
        offlineGroup.stream().
                filter(d -> d.isOffline()).
                filter(d -> !previouslyDetectedOffline.contains(d)).collect(Collectors.toList()).
                forEach((device) -> {
                    previouslyDetectedOffline.add(device);
                    messageList.add(device);
                });

        messenger.sendMessages(messageList);
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
            if(response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                return false;
            }
            //anything else means it's there and refusing to talk to us
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