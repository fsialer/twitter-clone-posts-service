package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response.UserResponse;
import reactor.core.publisher.Flux;

public interface UserWebClient {
    Flux<UserResponse> findFollowedByFollowerId(String followerId);
}
