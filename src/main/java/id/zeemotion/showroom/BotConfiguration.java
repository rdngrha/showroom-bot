package id.zeemotion.showroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

@Configuration
public class BotConfiguration
{
    @Autowired private Environment env;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        String token = env.getProperty("app.token");
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
            .build()
            .login()
            .block();
    
        return client;
    }
}
