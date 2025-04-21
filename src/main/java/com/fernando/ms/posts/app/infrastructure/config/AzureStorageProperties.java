package com.fernando.ms.posts.app.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure.storage")
@Getter
@Setter
public class AzureStorageProperties {
    private String accountName;
    private String accountKey;
    private String containerName;
}
