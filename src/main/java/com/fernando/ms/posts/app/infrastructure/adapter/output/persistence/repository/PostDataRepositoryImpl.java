package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostDataRepositoryImpl implements PostDataRepositoryCustom{
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private static final String ATTRIBUTE_POST_ID="postId";

    @Override
    public Mono<Long> countPostDataByPostId(String postId) {
        Query query=new Query(Criteria.where(ATTRIBUTE_POST_ID).is(postId));
        return reactiveMongoTemplate.count(query, PostDataDocument.class);
    }
}
