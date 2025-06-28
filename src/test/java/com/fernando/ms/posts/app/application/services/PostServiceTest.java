package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.AuthorNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.domain.models.Post;
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
class PostServiceTest {
    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostService postService;

    @Mock
    private ExternalUserOutputPort externalUserOutputPort;

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
        Author author=TestUtilAuthor.buildAuthorMock();
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.just(post));
        when(externalUserOutputPort.findByUserId(anyString())).thenReturn(Mono.just(author));
        Mono<Post> postMono=postService.findById("1");
        StepVerifier.create(postMono)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
        Mockito.verify(externalUserOutputPort,times(1)).findByUserId(anyString());
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
        Mockito.verify(externalUserOutputPort,never()).findByUserId(anyString());
    }

    @Test
    @DisplayName("When Post Is Saved Successfully Expect Post Information Correct")
    void When_PostIsSavedSuccessfully_Expect_PostInformationCorrect() {
        Post post = TestUtilPost.buildPostMock();
        when(postPersistencePort.save(any(Post.class))).thenReturn(Mono.just(post));

        Mono<Post> savedPost = postService.save(post);

        StepVerifier.create(savedPost)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort, times(1)).save(any(Post.class));
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
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.empty());
        Mono<Post> updatePost=postService.update("1",post);
        StepVerifier.create(updatePost)
                .expectError(PostNotFoundException.class)
                .verify();
        Mockito.verify(postPersistencePort,times(0)).save(any(Post.class));
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
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

    @Test
    @DisplayName("when User Authenticated Had UserFollowed Expect List Posts Recents")
    void when_UserAuthenticatedHadUserFollowed_Expect_ListPostsRecents() {
        String userId = "user123";
        int page = 0;
        int size = 10;

        Author author1 = TestUtilAuthor.buildAuthorMock();
        author1.setId("d44d5d7sd5sd4s5d");
        author1.setUserId("dsds5415");

        Post post1 = TestUtilPost.buildPostMock();
        post1.setUserId("dsds5415");

        when(externalUserOutputPort.findAuthorByUserId(userId))
                .thenReturn(Flux.just(author1));

        when(postPersistencePort.recent(List.of(author1), page, size))
                .thenReturn(Flux.just(post1));

        Flux<Post> result =postService.recent(userId, page, size);
        StepVerifier.create(result)
                .expectNextMatches(post -> post.getAuthor().equals(author1))
                .verifyComplete();

        verify(externalUserOutputPort).findAuthorByUserId(userId);
        verify(postPersistencePort).recent(List.of(author1),page, size);
    }

    @Test
    @DisplayName("when User Authenticated Had UserFollowed Expect List Posts me")
    void when_UserAuthenticatedHadUserFollowed_Expect_ListPostsMe() {
        String userId = "user123";
        int page = 0;
        int size = 10;

        Author author1 = TestUtilAuthor.buildAuthorMock();
        author1.setId("d44d5d7sd5sd4s5d");

        Post post1 = TestUtilPost.buildPostMock();
        post1.setUserId("d44d5d7sd5sd4s5d");

        when(externalUserOutputPort.findByUserId(userId))
                .thenReturn(Mono.just(author1));

        when(postPersistencePort.me(userId, page, size))
                .thenReturn(Flux.just(post1));

        Flux<Post> result =postService.me(userId, page, size);
        StepVerifier.create(result)
                .expectNextMatches(post -> post.getAuthor().equals(author1))
                .verifyComplete();

        verify(externalUserOutputPort).findByUserId(userId);
        verify(postPersistencePort).me(userId,page, size);
    }

    @Test
    @DisplayName("Expect AuthorNotFoundException When UserId Is Not Invalid")
    void Expect_AuthorNotFoundException_When_UserIdIsNotInvalid() {
        String userId = "user123";
        int page = 0;
        int size = 10;

        Author author1 = TestUtilAuthor.buildAuthorMock();
        author1.setId("d44d5d7sd5sd4s5d");

        Post post1 = TestUtilPost.buildPostMock();
        post1.setUserId("d44d5d7sd5sd4s5d");

        when(externalUserOutputPort.findByUserId(userId))
                .thenReturn(Mono.empty());

        Flux<Post> result =postService.me(userId, page, size);
        StepVerifier.create(result)
                .expectError(AuthorNotFoundException.class)
                .verify();

        verify(externalUserOutputPort,times(1)).findByUserId(userId);
        verify(postPersistencePort,times(0)).me(userId,page, size);
    }

    @Test
    @DisplayName("When User Exists Expect Count Post Published")
    void When_UserExists_Expect_CountPostPublished(){
        when(postPersistencePort.countPostByUser(anyString())).thenReturn(Mono.just(2L));
        Mono<Long> result=postService.countPostByUser("1");
        StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    @DisplayName("When PostId And UserId Is Valid Expect True")
    void When_PostIdAndUserIdIsValid_Expect_True() {
        when(postPersistencePort.verifyPostByIdUserId(anyString(),anyString())).thenReturn(Mono.just(true));
        Mono<Boolean> result = postService.verifyPostByUserId("678318b2c8dda45d9a6c300d","dsdsds5d1s5d02s51ds");
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(postPersistencePort, times(1)).verifyPostByIdUserId(anyString(),anyString());
    }

    @Test
    @DisplayName("When PostId And UserId Is Not Valid Expect False")
    void When_PostIdAndUserIdIsNotValid_Expect_False() {
        when(postPersistencePort.verifyPostByIdUserId(anyString(),anyString())).thenReturn(Mono.just(false));
        Mono<Boolean> result = postService.verifyPostByUserId("678318b2c8dda45d9a6c300d","dsdsds5d1s5d02s51ds");
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        Mockito.verify(postPersistencePort, times(1)).verifyPostByIdUserId(anyString(),anyString());
    }
}
