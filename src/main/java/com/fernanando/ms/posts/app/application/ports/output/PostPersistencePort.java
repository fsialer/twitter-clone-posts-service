package com.fernanando.ms.posts.app.application.ports.output;

import com.fernanando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Flux;

public interface PostPersistencePort {
    Flux<Post> findAll();
}
