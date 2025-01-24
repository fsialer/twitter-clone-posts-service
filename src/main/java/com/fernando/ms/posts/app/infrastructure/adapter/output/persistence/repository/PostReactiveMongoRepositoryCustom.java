package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;
import reactor.core.publisher.Flux;

public interface PostReactiveMongoRepositoryCustom {
    Flux<PostDocument> findAllUserAndPageAndSize(PostUser postUser,Long size,Long page);
}
