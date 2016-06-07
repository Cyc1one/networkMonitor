package uk.co.solutions4j.network.device.health.model;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "device")
public class Device {

    private String name;
    private URI url;
    private String failureMessage;
    private boolean offline = false;

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

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public String getFailedString(){
        if(offline){
            return name+": "+failureMessage;
        }
        return "";
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline){
        this.offline = offline;
    }


}
