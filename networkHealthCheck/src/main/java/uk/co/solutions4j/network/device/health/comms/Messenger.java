package uk.co.solutions4j.network.device.health.comms;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import uk.co.solutions4j.network.device.health.model.Device;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Messenger {

    private static Twitter twitter;
    private static String myScreenName;
    private static Facebook facebook;

    @Value("${spring.social.twitter.appId}")
    private String appId;
    @Value("${spring.social.twitter.appSecret}")
    private String appSecret;
    @Value("${spring.social.twitter.accessToken}")
    private String accessToken;
    @Value("${spring.social.twitter.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${spring.social.facebook.appId}")
    private String facebookAppId;

    @Value("${spring.social.facebook.appSecret}")
    private String facebookAppSecret;



    @PostConstruct
    public void init(){
        twitter = new TwitterTemplate(appId, appSecret, accessToken, accessTokenSecret);
        myScreenName = twitter.userOperations().getScreenName();

        //facebook = new FacebookTemplate();
    }

    public void sendMessage(Device device, String message){
        sendTweet(device, message);
    }

    public void sendMessages(List<Device> devices){
        if(devices.size()>0) {
            sendTweet(devices.stream()
                    .map(Device::getFailedString)
                    .collect(Collectors.joining("\n")) + " #networkMonitor #deviceDown");
        }
    }

    public void sendTweet(String message){
        try{
            twitter.timelineOperations().updateStatus("@" + myScreenName + message);
        } catch (DuplicateStatusException e){
            //twitter.directMessageOperations().sendDirectMessage(myScreenName, message);
        }
    }


    public void sendTweet(Device device, String message){
        //twitter.directMessageOperations().sendDirectMessage(myScreenName, "Device " + device.getName() + message);
        String completeMessage = "@" + myScreenName + " Device "+getDeviceDownString(device, message)+ " #networkMonitor #deviceDown";
        try{
        	System.out.print("Sending tweet\n"+completeMessage);
            twitter.timelineOperations().updateStatus(completeMessage);
        } catch (DuplicateStatusException e){
        	twitter.directMessageOperations().sendDirectMessage(myScreenName, "Device " + device.getName() + message);
        }
    }

    private String getDeviceDownString(Device device, String message) {
        return " " + device.getName() + message ;
    }

    public void sendFacebookStatus(Device device, String message){
        facebook.feedOperations().updateStatus("Device "+device.getName()+message);
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

}
