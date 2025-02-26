package com.fernando.ms.posts.app.application.services.proxy;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleUpdatePostProxy implements IProcess{
    private final ExternalUserOutputPort externalUserOutputPort;
    private final PostPersistencePort postPersistencePort;
    private final String id;
    @Override
    public Mono<Post> doProcess(Post post) {
        return externalUserOutputPort.verify(post.getUser().getId())
                .flatMap(exist->{
                    if(Boolean.FALSE.equals(exist)){
                        return Mono.error(new UserNotFoundException());
                    }
                    return postPersistencePort.findById(id)
                            .switchIfEmpty(Mono.error(PostNotFoundException::new))
                            .flatMap(postUpdated->{
                                postUpdated.setContent(post.getContent());
                                return Mono.just(postUpdated);
                            });
                });
    }
}
