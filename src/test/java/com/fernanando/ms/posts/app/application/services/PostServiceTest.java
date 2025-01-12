package com.fernanando.ms.posts.app.application.services;

import com.fernanando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernanando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.utils.TestUtilPost;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostService postService;

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

}
