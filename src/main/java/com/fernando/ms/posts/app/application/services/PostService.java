package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PostService implements PostInputPort {
    private final PostPersistencePort postPersistencePort;

    @Override
    public Flux<Post> findAll() {
        return postPersistencePort.findAll();
    }

    @Override
    public Mono<Post> findById(String id) {
        return postPersistencePort.findById(id).switchIfEmpty(Mono.error(PostNotFoundException::new));
    }

    @Override
    public Mono<Post> save(Post post) {
        return postPersistencePort.save(post);
    }

    @Override
    public Mono<Post> update(String id, Post post) {
        return postPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostNotFoundException::new))
                .flatMap(postUpdated->{
                    postUpdated.setContent(post.getContent());
                    return postPersistencePort.save(postUpdated);
                });
    }
}
