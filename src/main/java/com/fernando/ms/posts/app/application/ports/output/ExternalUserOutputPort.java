package com.fernando.ms.posts.app.application.ports.output;


import com.fernando.ms.posts.app.domain.models.User;
import reactor.core.publisher.Mono;

public interface ExternalUserOutputPort {
    Mono<Boolean> verify(Long id);
    Mono<User> findById(Long id);
}
