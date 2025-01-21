package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.services.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.UserRestClientMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.VerifyUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements ExternalUserOutputPort {

    private final WebClient webClient;
    private final UserRestClientMapper userRestClientMapper;

    @Override
    public Mono<Boolean> verify(Long id) {
        return webClient
                .get()
                .uri("/users/{id}/verify", id)
                .retrieve()
                .bodyToMono(VerifyUserResponse.class)
                .flatMap(verifyUserResponse->{
                    return Mono.just(verifyUserResponse.getExists());
                });
    }
}
