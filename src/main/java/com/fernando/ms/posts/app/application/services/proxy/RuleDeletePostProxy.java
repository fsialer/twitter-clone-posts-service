package com.fernando.ms.posts.app.application.services.proxy;

import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleDeletePostProxy implements IProcess{
    private final PostPersistencePort postPersistencePort;
    private final String id;

    @Override
    public Mono<Post> doProcess(Post post) {
        return postPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostNotFoundException::new));
    }
}
