package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.utils.TestUtilAuthor;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostReactiveMongoRepositoryImplTest {

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private PostReactiveMongoRepositoryImpl postReactiveMongoRepository;

    @Test
    @DisplayName("When Find Post By Authors And Pagination_ Expect List Of Documents")
    void When_FindPostByAuthorsAndPagination_Expect_ListOfDocuments() {
        Author author1 = TestUtilAuthor.buildAuthorMock();
        Author author2 = TestUtilAuthor.buildAuthorMock();
        author2.setId("4d786ds8sd56sd");
        author2.setNames("John");
        author2.setLastNames("Doe");
        PostDocument postDocument1 = TestUtilPost.buildPostDocumentMock();
        PostDocument postDocument2 = TestUtilPost.buildPostDocumentMock();
        postDocument2.setId("d854gorfd4");
        postDocument2.setContent("contenido 2");

        when(reactiveMongoTemplate.find(any(Query.class), any(Class.class)))
                .thenReturn(Flux.just(postDocument1, postDocument2));

        Flux<PostDocument> result = postReactiveMongoRepository.findPostByAuthorsAndPagination(List.of(author1, author2), 1, 10);

        StepVerifier.create(result)
                .expectNext(postDocument1)
                .expectNext(postDocument2)
                .verifyComplete();
    }
}
