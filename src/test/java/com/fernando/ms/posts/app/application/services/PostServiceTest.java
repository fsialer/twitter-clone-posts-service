package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.output.ExternalFollowerOutputPort;
import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import com.fernando.ms.posts.app.utils.TestUtilsUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostService postService;

    @Mock
    private ExternalUserOutputPort externalUserOutputPort;

    @Mock
    private ExternalFollowerOutputPort externalFollowerOutputPort;

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
        Post post= TestUtilPost.buildPostMock();
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
        when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(true));
        Mono<Post> savedPost = postService.save(post);

        StepVerifier.create(savedPost)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort, times(1)).save(any(Post.class));
        Mockito.verify(externalUserOutputPort, Mockito.times(1)).verify(anyLong());
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Does Not Exist")
    void Expect_UserNotFoundException_When_User_Does_Not_Exist() {
        Post post = TestUtilPost.buildPostMock();
        when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(false));
        Mono<Post> savedPost = postService.save(post);

        StepVerifier.create(savedPost)
                .expectError(UserNotFoundException.class)
                .verify();
        Mockito.verify(postPersistencePort,Mockito.never()).save(any(Post.class));
        Mockito.verify(externalUserOutputPort, Mockito.times(1)).verify(anyLong());
    }

    @Test
    @DisplayName("When Post Is Update Except Post Information Save Correctly")
    void When_PostIsUpdateExcept_PostInformationSaveCorrectly(){
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
    @DisplayName("When Post User Exists Expect List of Posts")
    void When_PostUserExists_Expect_ListOfPosts() {
        Post post = TestUtilPost.buildPostMock();
        User user = TestUtilsUser.buildUserMock();

        when(externalUserOutputPort.findById(anyLong())).thenReturn(Mono.just(user));
        when(postPersistencePort.findAllPostMe(any(User.class), anyLong(), anyLong())).thenReturn(Flux.just(post));

        Flux<Post> posts = postService.findAllPostMe(1L, 10L, 0L);

        StepVerifier.create(posts)
                .expectNext(post)
                .verifyComplete();

        Mockito.verify(externalUserOutputPort, Mockito.times(1)).findById(anyLong());
        Mockito.verify(postPersistencePort, Mockito.times(1)).findAllPostMe(any(User.class), anyLong(), anyLong());
    }

    @Test
    @DisplayName("When Follower Id Is Provided Expect List Of Recent Posts")
    void When_FollowerIdIsProvided_Expect_ListOfRecentPosts() {
        Post post = TestUtilPost.buildPostMock();
        Follower follower =Follower.builder()
                .followed(1L)
                .follower(2L)
                .build();
        User user = TestUtilsUser.buildUserMock();

        when(externalFollowerOutputPort.findFollowedByFollower(anyLong(), anyLong(), anyLong()))
                .thenReturn(Flux.just(follower));
        when(postPersistencePort.findAllPostRecent(anyList(), anyLong(), anyLong()))
                .thenReturn(Flux.just(post));
        when(externalUserOutputPort.findById(anyLong()))
                .thenReturn(Mono.just(user));

        Flux<Post> posts = postService.findAllPostRecent(1L, 10L, 0L);

        StepVerifier.create(posts)
                .expectNextMatches(p -> p.getUser().getId().equals(user.getId()))
                .verifyComplete();

        Mockito.verify(externalFollowerOutputPort, times(1)).findFollowedByFollower(anyLong(), anyLong(), anyLong());
        Mockito.verify(postPersistencePort, times(1)).findAllPostRecent(anyList(), anyLong(), anyLong());
        Mockito.verify(externalUserOutputPort, times(1)).findById(anyLong());
    }




}
