package com.fernanando.ms.posts.app.application.ports.input;

import com.fernanando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostInputPort {
    Flux<Post> findAll();
    Mono<Post> findById(String id);
}
