package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PostDataRepository extends ReactiveMongoRepository<PostDataDocument,String>, PostDataRepositoryCustom {
    Mono<Boolean> existsByPostIdAndUserId(String postId, String userId);
    Mono<PostDataDocument> findByPostIdAndUserId(String postId, String userId);
}
