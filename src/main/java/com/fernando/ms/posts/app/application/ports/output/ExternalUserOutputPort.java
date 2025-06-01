package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExternalUserOutputPort {
    Flux<Author> findAuthorByUserId(String userId);
    Mono<Author> findByUserId(String userId);
}
