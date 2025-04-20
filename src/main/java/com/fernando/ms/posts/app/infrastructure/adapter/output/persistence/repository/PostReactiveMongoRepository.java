package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostReactiveMongoRepository extends ReactiveMongoRepository<PostDocument,String>
, PostReactiveMongoRepositoryCustom {
}
