package com.fernanando.ms.posts.app.application.ports.output;

import com.fernanando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostPersistencePort {
    Flux<Post> findAll();
    Mono<Post> findById(String id);
    Mono<Post> save(Post post);
}
