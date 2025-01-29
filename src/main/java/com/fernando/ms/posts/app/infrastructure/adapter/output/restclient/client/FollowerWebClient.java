package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.FollowerResponse;
import reactor.core.publisher.Flux;

public interface FollowerWebClient {
    Flux<FollowerResponse> findFollowed(Long followerId);
}
