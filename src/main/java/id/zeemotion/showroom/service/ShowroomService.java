package id.zeemotion.showroom.service;

import java.math.BigInteger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import id.zeemotion.showroom.BotConfiguration;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShowroomService
{
    @Autowired private BotConfiguration botConfiguration;
    @Autowired private Environment env;
    Boolean isLivePosted = false, isNextLiveScheduled = false;
    
    public void getShowroomData(){
        GatewayDiscordClient discordClient = botConfiguration.gatewayDiscordClient();
        Boolean isLive = getIsLive();
        String nextLiveSchedule = getIsScheduledToLive();

        if (isLive && !isLivePosted){
            discordClient.getChannelById(Snowflake.of(BigInteger.valueOf(Long.valueOf(env.getProperty("app.channel.id")))))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage("Zee/ジー（JKT48） Broadcasting! @everyone\nhttps://www.showroom-live.com/JKT48_Zee"))
                .subscribe();
            isLivePosted = true;
        } else if (!isLive && isLivePosted) {
            isLivePosted = false;
        }
        if (!nextLiveSchedule.equals("TBD") && !isNextLiveScheduled){
            discordClient.getChannelById(Snowflake.of(BigInteger.valueOf(Long.valueOf(env.getProperty("app.channel.id")))))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage("Zee/ジー（JKT48） mau showroom nich: "+ nextLiveSchedule + "! @everyone\nhttps://pbs.twimg.com/media/FKWmtElUYAQ0WZE?format=jpg&name=large"))
                .subscribe();
            isNextLiveScheduled = true;
        } else if (nextLiveSchedule.equals("TBD") && isNextLiveScheduled) {
            isNextLiveScheduled = false;
        }
    }

    private Boolean getIsLive(){
        final String baseUrl = "https://www.showroom-live.com";
        final String url = baseUrl + "/api/room/profile?room_id=" + env.getProperty("app.room.id");
        
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JSONObject profileObject = new JSONObject(response.getBody());

            log.info("Is Azizi live: " + profileObject.getBoolean("is_onlive"));
            log.info("Is live info posted: " + isLivePosted);
            return profileObject.getBoolean("is_onlive");
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private String getIsScheduledToLive(){
        final String baseUrl = "https://www.showroom-live.com";
        final String url = baseUrl + "/api/room/next_live?room_id=" + env.getProperty("app.room.id");
        
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JSONObject profileObject = new JSONObject(response.getBody());

            log.info("Next scheduled live: " + profileObject.getString("text"));
            log.info("Is next schedule info posted: " + isNextLiveScheduled);
            return profileObject.getString("text");
        } catch (Exception e) {
            log.error(e.getMessage());
            return "TBD";
        }
    }
}
