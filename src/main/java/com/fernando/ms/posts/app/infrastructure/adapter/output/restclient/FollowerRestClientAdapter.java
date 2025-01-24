package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.application.ports.output.ExternalFollowerOutputPort;
import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.FollowerRestClientMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.FollowerResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FollowerRestClientAdapter implements ExternalFollowerOutputPort {
    private final WebClient webClientFollower;
    private final FollowerRestClientMapper followerRestClientMapper;
    @Override
    public Flux<Follower> findFollowedByFollower(Long followerId, Long page, Long size) {

        return webClientFollower
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/followers/find-followed-by-follower/{followerId}")
                        .build(followerId))
                .retrieve()
                .bodyToFlux(FollowerResponse.class)
                .flatMap(followerRestClientMapper::toFollower);
    }
}
