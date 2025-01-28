package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.application.ports.output.ExternalFollowerOutputPort;
import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.FollowerWebClient;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.FollowerRestClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FollowerRestClientAdapter implements ExternalFollowerOutputPort {
    private final WebClient webClientFollower;
    private final FollowerRestClientMapper followerRestClientMapper;
    private final FollowerWebClient followerWebClient;

    @Override
    public Flux<Follower> findFollowedByFollower(Long followerId) {
        return followerWebClient.findFollowed(followerId)
                .flatMap(followerRestClientMapper::toFollower);
    }
}
