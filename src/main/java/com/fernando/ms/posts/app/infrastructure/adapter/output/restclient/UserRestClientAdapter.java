package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.UserRestClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements ExternalUserOutputPort {

    private final UserRestClientMapper userRestClientMapper;
    private final UserWebClient userWebClient;

    @Override
    public Mono<Boolean> verify(Long id) {
        return userWebClient.verify(id)
                .flatMap(existsUserResponse -> {
                    return Mono.just(existsUserResponse.getExists());
                });
    }

    @Override
    public Mono<User> findById(Long id) {
        return userWebClient.findById(id)
                .map(userRestClientMapper::toUser);
    }
}
