package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.output.PostDataPersistencePort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostDataNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.PostRuleException;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostDataServiceTest {

    @Mock
    private PostDataPersistencePort postDataPersistencePort;

    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostDataService postDataService;

    @Test
    @DisplayName("When PostsData Information Is Correct Expect Save Information")
    void When_PostDataInformationIsCorrect_Expect_SaveInformation(){
        PostData postData= TestUtilPostData.buildPostDataMock();
        Post post= TestUtilPost.buildPostMock();

        when(postPersistencePort.findById(anyString())).thenReturn(Mono.just(post));
        when(postDataPersistencePort.verifyPostData(anyString(),anyString())).thenReturn(Mono.just(Boolean.FALSE));
        when(postDataPersistencePort.save(postData)).thenReturn(Mono.empty());

        Mono<Void> posts=postDataService.save(postData);
        StepVerifier.create(posts)
                .verifyComplete();
        Mockito.verify(postDataPersistencePort,times(1)).save(any(PostData.class));
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
        Mockito.verify(postDataPersistencePort,times(1)).verifyPostData(anyString(),anyString());
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Post Does Not Exists")
    void Expect_PostNotFoundException_When_PostDoesNotExists(){
        PostData postData= TestUtilPostData.buildPostDataMock();
        when(postPersistencePort.findById(anyString())).thenReturn(Mono.empty());
        Mono<Void> posts=postDataService.save(postData);
        StepVerifier.create(posts)
                .expectError(PostNotFoundException.class)
                .verify();
        Mockito.verify(postDataPersistencePort,never()).save(any(PostData.class));
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
        Mockito.verify(postDataPersistencePort,never()).verifyPostData(anyString(),anyString());
    }

    @Test
    @DisplayName("Expect PostRuleException When PostData Already Exists")
    void Expect_PostRuleException_When_PostDataAlreadyExists(){
        PostData postData= TestUtilPostData.buildPostDataMock();
        Post post= TestUtilPost.buildPostMock();

        when(postPersistencePort.findById(anyString())).thenReturn(Mono.just(post));
        when(postDataPersistencePort.verifyPostData(anyString(),anyString())).thenReturn(Mono.just(Boolean.TRUE));

        Mono<Void> posts=postDataService.save(postData);
        StepVerifier.create(posts)
                .expectError(PostRuleException.class)
                .verify();
        Mockito.verify(postDataPersistencePort,never()).save(any(PostData.class));
        Mockito.verify(postPersistencePort,times(1)).findById(anyString());
        Mockito.verify(postDataPersistencePort,times(1)).verifyPostData(anyString(),anyString());
    }

    @Test
    @DisplayName("when PostExists Expect Delete Successfully")
    void when_PostExists_Expect_DeleteSuccessfully() {

        String postId = "postId123";
        String userId="userIdfdfd4";
        PostData postData = TestUtilPostData.buildPostDataMock();

        when(postDataPersistencePort.findByPostIdAndUserId(anyString(),anyString())).thenReturn(Mono.just(postData));
        when(postDataPersistencePort.delete(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = postDataService.delete(postId,userId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(postDataPersistencePort, times(1)).findByPostIdAndUserId(anyString(),anyString());
        verify(postDataPersistencePort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Post Does Not Exist")
    void Expect_PostNotFoundException_When_PostDoesNotExist() {
        String postId = "postId123";
        String userId="userIdfdfd4";
        when(postDataPersistencePort.findByPostIdAndUserId(anyString(),anyString())).thenReturn(Mono.empty());
        Mono<Void> result = postDataService.delete(postId,userId);

        StepVerifier.create(result)
                .expectError(PostDataNotFoundException.class)
                .verify();

        verify(postDataPersistencePort, times(1)).findByPostIdAndUserId(anyString(),anyString());
        verify(postDataPersistencePort, never()).delete(anyString());
    }

    @Test
    @DisplayName("When PostId Exists Expect Count PostData By post")
    void When_PostIdExists_Expect_CountPostDataByPost(){
        when(postDataPersistencePort.countPostDataByPost(anyString())).thenReturn(Mono.just(2L));
        Mono<Long> result=postDataService.countPostDataByPost("1");
        StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    @DisplayName("When PostId and UserId Exists Expect True")
    void When_PostIdAndUserIdExists_Expect_True(){
        when(postDataPersistencePort.verifyPostData(anyString(),anyString())).thenReturn(Mono.just(true));
        Mono<Boolean> result=postDataService.verifyExistsPostData("1","1");
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

}
