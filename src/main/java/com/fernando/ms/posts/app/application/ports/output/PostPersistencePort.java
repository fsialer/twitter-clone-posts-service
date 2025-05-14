package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.domain.models.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostPersistencePort {
    Flux<Post> findAll();
    Mono<Post> findById(String id);
    Mono<Post> save(Post post);
    Mono<Void> delete(String id);
    Mono<Boolean> verify(String id);
    Flux<Post> recent(List<Author> authors,int page, int size);
}
