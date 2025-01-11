package com.fernanando.ms.posts.app.application.ports.input;

import com.fernanando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Flux;

public interface PostInputPort {
    Flux<Post> findAll();
}
