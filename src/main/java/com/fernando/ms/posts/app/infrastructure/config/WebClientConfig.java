package com.fernando.ms.posts.app.infrastructure.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${user-service.url}")
    private String apiUser;
    @Value("${follower-service.url}")
    private String apiFollower;
    @Bean
    public WebClient webClientUser(WebClient.Builder builder) {
        return builder.baseUrl(apiUser).build();
    }

    @Bean
    public WebClient webClientFollower(WebClient.Builder builder) {
        return builder.baseUrl(apiFollower).build();
    }

}
