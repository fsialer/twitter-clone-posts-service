package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.impl;

import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.FollowerWebClient;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.FollowerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FollowerWebClientImpl implements FollowerWebClient {
    private final WebClient webClientFollower;
    @Override
    public Flux<FollowerResponse> findFollowed(Long followerId) {
        return webClientFollower
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find-followed-by-follower/{followerId}")
                        .build(followerId))
                .retrieve()
                .bodyToFlux(FollowerResponse.class);
    }
}
