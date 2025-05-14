package com.fernando.ms.posts.app.application.ports.input;

import com.fernando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostInputPort {
    Flux<Post> findAll();
    Mono<Post> findById(String id);
    Mono<Post> save(Post post);
    Mono<Post> update(String id,Post post);
    Mono<Void> delete(String id);
    Mono<Boolean> verify(String id);
    Flux<Post> recent(String userId,int page,int size);
    Flux<Post> me(String userId,int page,int size);
}
