package com.fernando.ms.posts.app.application.services.proxy;

import com.fernando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Mono;

public interface IProcess {
    Mono<Post> doProcess(Post post);
}
