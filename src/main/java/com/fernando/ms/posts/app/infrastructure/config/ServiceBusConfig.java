package com.fernando.ms.posts.app.infrastructure.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ServiceBusConfig {
    @Value("${service-bus.connection-string}")
    private String connectionString;

    @Value("${service-bus.topic-name}")
    private String topicName;

    @Bean
    public ServiceBusSenderClient serviceBusSenderClient() {
        return new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .topicName(topicName)
                .buildClient();
    }
}
