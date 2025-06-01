package com.fernando.ms.posts.app.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services.url")
@Getter
@Setter
public class ServiceProperties {
    private String usersService;
}
