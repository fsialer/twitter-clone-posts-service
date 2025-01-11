package com.fernanando.ms.posts.app.application.services;

import com.fernanando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernanando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernanando.ms.posts.app.domain.models.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class PostService implements PostInputPort {
    private final PostPersistencePort postPersistencePort;

    @Override
    public Flux<Post> findAll() {
        return postPersistencePort.findAll();
    }
}
