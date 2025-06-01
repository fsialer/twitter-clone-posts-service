package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import reactor.core.publisher.Mono;

public interface PostDataRepositoryCustom {
    Mono<Long> countPostDataByPostId(String userId);
}
