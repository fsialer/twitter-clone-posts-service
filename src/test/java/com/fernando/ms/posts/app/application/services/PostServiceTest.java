package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.bus.PostBusAdapter;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostService postService;

    @Mock
    private ExternalUserOutputPort externalUserOutputPort;

    @Mock
    private ExternalFollowerOutputPort externalFollowerOutputPort;

    @Mock
    private PostBusAdapter postBusAdapter;

    @Test
    @DisplayName("When Posts Information Is Correct Expect A List Posts")
    void When_UsersInformationIsCorrect_Expect_AListUsers(){
        Post post= TestUtilPost.buildPostMock();
        when(postPersistencePort.findAll()).thenReturn(Flux.just(post));

        Flux<Post> posts=postService.findAll();
        StepVerifier.create(posts)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort,times(1)).findAll();
    }

    @Test
    @DisplayName("When Post Identifier Is Correct Except Post Information Correct")
    void When_UserIdentifierIsCorrect_Except_UserInformationCorrect(){
        Post post= TestUtilPost.buildPostMock();
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.just(post));
        Mono<Post> postMono=postService.findById("1");
        StepVerifier.create(postMono)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Post Identifier Is Invalid")
    void Expect_PostNotFoundException_When_PostIdentifierIsInvalid(){
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.empty());
        Mono<Post> userMono=postService.findById("1");
        StepVerifier.create(userMono)
                .expectError(PostNotFoundException.class)
                .verify();
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
    }


    @Test
    @DisplayName("When Post Is Saved Successfully Expect Post Information Correct")
    void When_PostIsSavedSuccessfully_Expect_PostInformationCorrect() {
        Post post = TestUtilPost.buildPostMock();
        when(postPersistencePort.save(any(Post.class))).thenReturn(Mono.just(post));
        //doNothing().when(postBusAdapter).sendNotification(any(Post.class));
        Mono<Post> savedPost = postService.save(post);

        StepVerifier.create(savedPost)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort, times(1)).save(any(Post.class));
        //Mockito.verify(postBusAdapter, Mockito.times(1)).sendNotification(any(Post.class));
    }

    @Test
    @DisplayName("When Post Is Update Except Post Information Save Correctly")
    void When_PostIsUpdate_Except_PostInformationSaveCorrectly(){
        Post post=TestUtilPost.buildPostMock();
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.just(post));
        when(postPersistencePort.save(any(Post.class))).thenReturn(Mono.just(post));

        Mono<Post> updatePost=postService.update("1",post);
        StepVerifier.create(updatePost)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort,times(1)).save(any(Post.class));
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Updated Post Identifier Is Invalid")
    void Expect_PostNotFoundException_When_UpdatePostIdentifierIsInvalid(){
        Post post=TestUtilPost.buildPostMock();
        //when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(true));
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.empty());
        Mono<Post> updatePost=postService.update("1",post);
        StepVerifier.create(updatePost)
                .expectError(PostNotFoundException.class)
                .verify();
        Mockito.verify(postPersistencePort,times(0)).save(any(Post.class));
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
        //Mockito.verify(externalUserOutputPort,times(1)).verify(anyLong());
    }

    @Test
    @DisplayName("When Post Exists Expect Post Deleted Successfully")
    void When_PostExists_Expect_PostDeletedSuccessfully() {
        Post  post = TestUtilPost.buildPostMock();
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.just(post));
        when(postPersistencePort.delete(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = postService.delete("1");

        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(postPersistencePort, times(1)).findById(anyString());
        Mockito.verify(postPersistencePort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Post Does Not Exist")
    void Expect_PostNotFoundException_When_PostDoesNotExist() {
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = postService.delete("1");

        StepVerifier.create(result)
                .expectError(PostNotFoundException.class)
                .verify();

        Mockito.verify(postPersistencePort, times(1)).findById(anyString());
        Mockito.verify(postPersistencePort, Mockito.never()).delete(anyString());
    }

    @Test
    @DisplayName("When Post Verification Is Successful Expect Post Verified")
    void When_UserVerificationIsSuccessful_Expect_UserVerified() {

        when(postPersistencePort.verify(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = postService.verify("678318b2c8dda45d9a6c300d");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(postPersistencePort, times(1)).verify(anyString());
    }

    @Test
    @DisplayName("When Post Verification Is Incorrect Expect Post Do Not Verified")
    void When_UserVerificationIsIncorrect_Expect_UserDoNotVerified() {

        when(postPersistencePort.verify(anyString())).thenReturn(Mono.just(false));

        Mono<Boolean> result = postService.verify("678318b2c8dda45d9a6c300d");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        Mockito.verify(postPersistencePort, times(1)).verify(anyString());
    }
}
