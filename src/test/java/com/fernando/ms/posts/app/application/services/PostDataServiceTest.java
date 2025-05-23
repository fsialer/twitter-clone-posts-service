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
        PostData postData = TestUtilPostData.buildPostDataMock();

        when(postDataPersistencePort.findById(postId)).thenReturn(Mono.just(postData));
        when(postDataPersistencePort.delete(postId)).thenReturn(Mono.empty());

        Mono<Void> result = postDataService.delete(postId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(postDataPersistencePort, times(1)).findById(postId);
        verify(postDataPersistencePort, times(1)).delete(postId);
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Post Does Not Exist")
    void Expect_PostNotFoundException_When_PostDoesNotExist() {
        String postId = "postId123";
        when(postDataPersistencePort.findById(postId)).thenReturn(Mono.empty());
        Mono<Void> result = postDataService.delete(postId);

        StepVerifier.create(result)
                .expectError(PostDataNotFoundException.class)
                .verify();

        verify(postDataPersistencePort, times(1)).findById(postId);
        verify(postDataPersistencePort, never()).delete(postId);
    }

}
