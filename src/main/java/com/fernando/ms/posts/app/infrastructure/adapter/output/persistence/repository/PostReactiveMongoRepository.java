package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PostReactiveMongoRepository extends ReactiveMongoRepository<PostDocument,String>
, PostReactiveMongoRepositoryCustom {
    Flux<PostDocument> findAllByPostUserOrderByDatePostDesc(PostUser postUser);
}
