package com.fernando.ms.posts.app.application.ports.input;

import com.fernando.ms.posts.app.domain.models.PostData;
import reactor.core.publisher.Mono;

public interface PostDataInputPort {
    Mono<Void> save(PostData postData);
    Mono<Void> delete(String id);
    Mono<Long> countPostDataByPost(String postId);
    Mono<Boolean> verifyExistsPostData(String postId, String userId);
}
