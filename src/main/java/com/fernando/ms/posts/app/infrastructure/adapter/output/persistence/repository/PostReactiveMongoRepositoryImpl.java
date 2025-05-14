package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostReactiveMongoRepositoryImpl implements PostReactiveMongoRepositoryCustom{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<PostDocument> findPostByAuthorsAndPagination(List<Author> authors, int page, int size) {
        Set<String>ids=authors.stream().map(Author::getId).collect(Collectors.toSet());
        Query query=new Query(Criteria.where("userId").in(ids.stream().toList()));
        query.with(Sort.by(Sort.Direction.DESC,"datePost"))
                .skip((long) (page-1)*size)
                .limit(size);
        return reactiveMongoTemplate.find(query, PostDocument.class);
    }
}
