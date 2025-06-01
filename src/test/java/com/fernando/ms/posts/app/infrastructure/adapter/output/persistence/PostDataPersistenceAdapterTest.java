package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostDataPersistenceMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostDataRepository;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostDataPersistenceAdapterTest {
    @Mock
    private PostDataRepository postDataRepository;

    @Mock
    private PostDataPersistenceMapper postDataPersistenceMapper;

    @InjectMocks
    private PostDataPersistenceAdapter postDataPersistenceAdapter;

    @Test
    @DisplayName("When Posts Are Correct Expect A List Posts Correct")
    void When_UsersAreCorrect_Expect_AListUsersCorrect() {
        PostData postData= TestUtilPostData.buildPostDataMock();
        PostDataDocument postDataDocument= TestUtilPostData.buildPostDataDocumentMock();
        when(postDataPersistenceMapper.toPostDataDocument(postData)).thenReturn(postDataDocument);
        when(postDataRepository.save(postDataDocument)).thenReturn(Mono.just(postDataDocument));
        when(postDataPersistenceMapper.toPostData(any(Mono.class))).thenReturn(Mono.just(postData));

        Mono<Void> result = postDataPersistenceAdapter.save(postData);

        StepVerifier.create(result)
                .verifyComplete();

        verify(postDataRepository, times(1)).save(postDataDocument);
        verify(postDataPersistenceMapper, times(1)).toPostDataDocument(postData);
    }

    @Test
    @DisplayName("when Verify PostData Exists Expect True")
    void When_VerifyPostDataExists_Expect_True(){
        String postId = "postId123";
        String userId = "userId123";

        when(postDataRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(Mono.just(true));

        Mono<Boolean> result = postDataPersistenceAdapter.verifyPostData(postId, userId);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(postDataRepository, times(1)).existsByPostIdAndUserId(postId, userId);
    }

    @Test
    @DisplayName("When Find By Id Expect Return PostData")
    void when_FindById_Expect_ReturnPostData() {
        String postId = "1";
        PostDataDocument postDataDocument = TestUtilPostData.buildPostDataDocumentMock();
        PostData postData = TestUtilPostData.buildPostDataMock();

        when(postDataRepository.findById(anyString())).thenReturn(Mono.just(postDataDocument));
        when(postDataPersistenceMapper.toPostData(any(Mono.class))).thenReturn(Mono.just(postData));

        Mono<PostData> result = postDataPersistenceAdapter.findById(postId);

        StepVerifier.create(result)
                .expectNext(postData)
                .verifyComplete();

        verify(postDataRepository, times(1)).findById(anyString());
        verify(postDataPersistenceMapper, times(1)).toPostData(any(Mono.class));
    }

    @Test
    @DisplayName("when Delete By Id Expect Complete Successfully")
    void when_DeleteById_Expect_CompleteSuccessfully() {
        String postId = "postId123";
        when(postDataRepository.deleteById(postId)).thenReturn(Mono.empty());
        Mono<Void> result = postDataPersistenceAdapter.delete(postId);
        StepVerifier.create(result)
                .verifyComplete();

        verify(postDataRepository, times(1)).deleteById(postId);
    }

    @Test
    @DisplayName("When PostId Exits Expect Count PostData By PostId")
    void When_PostIdExists_Expect_CountPostDataByPostId() {
        when(postDataRepository.countPostDataByPostId(anyString())).thenReturn(Mono.just(2L));
        Mono<Long> result = postDataPersistenceAdapter.countPostDataByPost("1");

        StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
        verify(postDataRepository, times(1)).countPostDataByPostId(anyString());
    }
}
