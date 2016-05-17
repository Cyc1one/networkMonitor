package uk.co.solutions4j.network.device.health.model;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "device")
public class Device {

    private String name;
    private URI url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url.toString();
    }

    public URI getURI(){
        return url;
    }

    public void setUrl(String url) {
        this.url = URI.create(url);
    }
}
