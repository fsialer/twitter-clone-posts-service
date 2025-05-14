package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import reactor.core.publisher.Flux;

import java.util.List;

public interface PostReactiveMongoRepositoryCustom {
    Flux<PostDocument> findPostByAuthorsAndPagination(List<Author> authors, int page, int size);
}
