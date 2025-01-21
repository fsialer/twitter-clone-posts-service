package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostPersistenceMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostReactiveMongoRepository;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostPersistenceAdapterTest {

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

}
