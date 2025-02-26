package com.fernando.ms.posts.app.application.services.proxy;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleSavePostProxy implements IProcess{
    private final ExternalUserOutputPort externalUserOutputPort;

    @Override
    public Mono<Post> doProcess(Post post) {
        return externalUserOutputPort.verify(post.getUser().getId())
                .flatMap(exist->{
                    if(Boolean.FALSE.equals(exist)){
                        return Mono.error(new UserNotFoundException());
                    }
                    return Mono.just(post);
                });
    }
}
