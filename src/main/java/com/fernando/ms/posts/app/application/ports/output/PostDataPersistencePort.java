package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.PostData;
import reactor.core.publisher.Mono;

public interface PostDataPersistencePort {
    Mono<Void> save(PostData postData);
    Mono<PostData> findById(String id);
    Mono<Boolean> verifyPostData(String postId,String userId);
    Mono<Void> delete(String id);
    Mono<Long> countPostDataByPost(String postId);
}
