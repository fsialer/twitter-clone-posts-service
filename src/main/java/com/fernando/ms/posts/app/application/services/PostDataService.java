package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.input.PostDataInputPort;
import com.fernando.ms.posts.app.application.ports.output.PostDataPersistencePort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostDataNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.PostRuleException;
import com.fernando.ms.posts.app.domain.models.PostData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostDataService implements PostDataInputPort {
    private final PostDataPersistencePort postDataPersistencePort;
    private final PostPersistencePort postPersistencePort;
    @Override
    public Mono<Void> save(PostData postData) {
        return postPersistencePort.findById(postData.getPostId())
                        .switchIfEmpty(Mono.error(PostNotFoundException::new))
                        .flatMap(postDataInfo->postDataPersistencePort.verifyPostData(postData.getPostId(),postData.getUserId())
                                    .filter(Boolean.FALSE::equals)
                                    .switchIfEmpty(Mono.error(new PostRuleException("PostData already exists")))
                                    .flatMap(verify->postDataPersistencePort.save(postData)));
    }

    @Override
    public Mono<Void> delete(String id) {
        return postDataPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostDataNotFoundException::new))
                .flatMap(postData->postDataPersistencePort.delete(id));
    }

    @Override
    public Mono<Long> countPostDataByPost(String postId) {
        return postDataPersistencePort.countPostDataByPost(postId);
    }
}
