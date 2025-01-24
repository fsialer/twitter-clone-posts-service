package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostPersistencePort {
    Flux<Post> findAll();
    Mono<Post> findById(String id);
    Mono<Post> save(Post post);
    Mono<Void> delete(String id);
    Mono<Boolean> verify(String id);
    Flux<Post> findAllPostMe(User user);
    Flux<Post> findAllPostMe(User user,Long size,Long page);
    Flux<Post> findAllPostRecent(Iterable<User> followed,Long size,Long page);

}
