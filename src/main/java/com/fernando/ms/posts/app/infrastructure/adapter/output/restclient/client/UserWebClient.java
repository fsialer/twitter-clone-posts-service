package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import reactor.core.publisher.Mono;

public interface UserWebClient {
    Mono<ExistsUserResponse> verify(Long id);
    Mono<UserResponse> findById(Long id);
}
