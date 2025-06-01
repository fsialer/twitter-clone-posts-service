package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostDataRepositoryImplTest {
    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private PostDataRepositoryImpl postDataRepositoryImpl;

    @Test
    @DisplayName("When PostId Exists Expect Count PostData By PostId")
    void When_PostIdExists_Expect_CountPostDataByPostId(){
        String postId = "1";
        Long expectedCount = 5L;

        when(reactiveMongoTemplate.count(any(Query.class), eq(PostDataDocument.class)))
                .thenReturn(Mono.just(expectedCount));

        Mono<Long> result = postDataRepositoryImpl.countPostDataByPostId(postId);

        StepVerifier.create(result)
                .expectNext(expectedCount)
                .verifyComplete();

        verify(reactiveMongoTemplate, times(1)).count(any(Query.class), eq(PostDataDocument.class));
    }
}
