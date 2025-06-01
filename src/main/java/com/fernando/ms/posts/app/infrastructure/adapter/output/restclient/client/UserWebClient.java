package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserWebClient {
    Flux<UserResponse> findFollowedByFollowerId(String followerId);
    Mono<UserResponse> me(String userId);
}
