package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostPersistenceMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostReactiveMongoRepository;
import com.fernando.ms.posts.app.utils.TestUtilAuthor;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostPersistenceAdapterTest {

    @Mock
    private PostReactiveMongoRepository postReactiveMongoRepository;

    @Mock
    private PostPersistenceMapper postPersistenceMapper;

    @InjectMocks
    private PostPersistenceAdapter postPersistenceAdapter;

    @Test
    @DisplayName("When Posts Are Correct Expect A List Posts Correct")
    void When_UsersAreCorrect_Expect_AListUsersCorrect() {
        Post post= TestUtilPost.buildPostMock();
        PostDocument postDocument= TestUtilPost.buildPostDocumentMock();
        when(postReactiveMongoRepository.findAll()).thenReturn(Flux.just(postDocument));
        when(postPersistenceMapper.toPosts(any(Flux.class))).thenReturn(Flux.just(post));
        Flux<Post> result = postPersistenceAdapter.findAll();
        StepVerifier.create(result)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postReactiveMongoRepository,times(1)).findAll();
        Mockito.verify(postPersistenceMapper,times(1)).toPosts(any(Flux.class));
    }

    @Test
    @DisplayName("When Post Identifier Is Correct Expect Post Information Correct")
    void When_UserIdentifierIsCorrect_Expect_UserInformationCorrect(){
        Post user= TestUtilPost.buildPostMock();
        PostDocument userEntity= TestUtilPost.buildPostDocumentMock();
        when(postReactiveMongoRepository.findById(anyString())).thenReturn(Mono.just(userEntity));
        when(postPersistenceMapper.toPost(any(PostDocument.class))).thenReturn(user);

        Mono<Post> result=postPersistenceAdapter.findById("1");
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(postReactiveMongoRepository,times(1)).findById(anyString());
        Mockito.verify(postPersistenceMapper,times(1)).toPost(any(PostDocument.class));
    }


    @Test
    @DisplayName("When Post Is Saved Successfully Expect Post Information Correct")
    void When_PostIsSavedSuccessfully_Expect_PostInformationCorrect() {
        Post post = TestUtilPost.buildPostMock();
        PostDocument postDocument = TestUtilPost.buildPostDocumentMock();

        when(postPersistenceMapper.toPostDocument(any(Post.class))).thenReturn(postDocument);
        when(postReactiveMongoRepository.save(any(PostDocument.class))).thenReturn(Mono.just(postDocument));
        when(postPersistenceMapper.toPost(any(Mono.class))).thenReturn(Mono.just(post));

        Mono<Post> savedPost = postPersistenceAdapter.save(post);

        StepVerifier.create(savedPost)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistenceMapper, times(1)).toPostDocument(any(Post.class));
        Mockito.verify(postReactiveMongoRepository, times(1)).save(any(PostDocument.class));
        Mockito.verify(postPersistenceMapper, times(1)).toPost(any(Mono.class));
    }

    @Test
    @DisplayName("When Post Exists Expect Post Deleted Successfully")
    void When_UserExists_Expect_UserDeletedSuccessfully() {
        when(postReactiveMongoRepository.deleteById(anyString())).thenReturn(Mono.empty());
        Mono<Void> result = postPersistenceAdapter.delete("1");
        StepVerifier.create(result)
                .verifyComplete();
        Mockito.verify(postReactiveMongoRepository, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("When Post Verification Is Successful Expect Post Verified")
    void When_PostVerificationIsSuccessful_Expect_PostVerified() {
        when(postReactiveMongoRepository.existsById(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = postPersistenceAdapter.verify("678318b2c8dda45d9a6c300d");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(postReactiveMongoRepository, times(1)).existsById(anyString());
    }

    @Test
    @DisplayName("When ExistsAuthors And Page Size Expect Correct Posts List")
    void When_ExistsAuthorsAndPapageSize_Expect_CorrectPostsList() {
        Author author = TestUtilAuthor.buildAuthorMock();
        Post post = TestUtilPost.buildPostMock();
        PostDocument postDocument = TestUtilPost.buildPostDocumentMock();

        when(postReactiveMongoRepository.findPostByAuthorsAndPagination(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.just(postDocument));
        when(postPersistenceMapper.toPosts(any(Flux.class)))
                .thenReturn(Flux.just(post));

        Flux<Post> result = postPersistenceAdapter.recent(List.of(author), 0, 10);

        StepVerifier.create(result)
                .expectNext(post)
                .verifyComplete();
        verify(postReactiveMongoRepository,times(1)).findPostByAuthorsAndPagination(anyList(), anyInt(), anyInt());
        verify(postPersistenceMapper,times(1)).toPosts(any(Flux.class));
    }

    @Test
    @DisplayName("When UserId And Pagination Expect List Post Correct")
    void When_UserIdAndPagination_Expect_ListPostCorrect() {
        Post post = TestUtilPost.buildPostMock();
        PostDocument postDocument = TestUtilPost.buildPostDocumentMock();

        when(postReactiveMongoRepository.findAllByUserIdAndPagination(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.just(postDocument));
        when(postPersistenceMapper.toPosts(any(Flux.class)))
                .thenReturn(Flux.just(post));

        Flux<Post> result = postPersistenceAdapter.me("d68sdg5h698d5", 1, 10);

        StepVerifier.create(result)
                .expectNext(post)
                .verifyComplete();
        verify(postReactiveMongoRepository,times(1)).findAllByUserIdAndPagination(anyString(), anyInt(), anyInt());
        verify(postPersistenceMapper,times(1)).toPosts(any(Flux.class));
    }

    @Test
    @DisplayName("When UserId Exits Expect Count Post By User")
    void When_UserIdExists_Expect_CountPostByUser(){
        when(postReactiveMongoRepository.countPostByUserId(anyString())).thenReturn(Mono.just(2L));
        Mono<Long> result=postPersistenceAdapter.countPostByUser("1");

        StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
        verify(postReactiveMongoRepository,times(1)).countPostByUserId(anyString());
    }

    @Test
    @DisplayName("When PostId And UserId Is Valid Expect True")
    void When_PostIdAndUserIdIsValid_Expect_True() {
        when(postReactiveMongoRepository.existsByPostIdAndUserId(anyString(),anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = postPersistenceAdapter.verifyPostByIdUserId("678318b2c8dda45d9a6c300d","d41dswyu2g4557df");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(postReactiveMongoRepository, times(1)).existsByPostIdAndUserId(anyString(),anyString());
    }

    @Test
    @DisplayName("When PostId And UserId Is Valid Expect False")
    void When_PostIdAndUserIdIsValid_Expect_False() {
        when(postReactiveMongoRepository.existsByPostIdAndUserId(anyString(),anyString())).thenReturn(Mono.just(false));

        Mono<Boolean> result = postPersistenceAdapter.verifyPostByIdUserId("678318b2c8dda45d9a6c300d","d41dswyu2g4557df");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        Mockito.verify(postReactiveMongoRepository, times(1)).existsByPostIdAndUserId(anyString(),anyString());
    }
}
