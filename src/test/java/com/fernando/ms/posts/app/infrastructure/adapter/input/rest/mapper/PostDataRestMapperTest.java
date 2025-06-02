package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.CountPostDataResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostDataResponse;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostDataRestMapperTest {

    private PostDataRestMapper postDataRestMapper;

    @BeforeEach
    void setUp(){
        postDataRestMapper= Mappers.getMapper(PostDataRestMapper.class);
    }

    @Test
    @DisplayName("When Mapping UserID And CreateDataRequest Expect PosData")
    void When_MappingUserIDAndCreateDataRequest_Expect_PosData() {
        CreatePostDataRequest createPostDataRequest = TestUtilPostData.buildCreatePostDataRequestMock();
        String userId = "454sadhdmd4857d4";
        PostData postData = postDataRestMapper.toPostData(userId, createPostDataRequest);
        assertEquals(userId,postData.getUserId());
        assertEquals(createPostDataRequest.getPostId(),postData.getPostId());
        assertEquals(createPostDataRequest.getTypeTarget(),postData.getTypeTarget());
    }

    @Test
    @DisplayName("When Mapping Long Expect MonoCountPostDataResponse")
    void When_MappingLong_Expect_MonoCountPostDataResponse(){
        Mono<CountPostDataResponse> countPostDataResponse=postDataRestMapper.toCountPostDataResponse(2L);
        StepVerifier.create(countPostDataResponse)
                .consumeNextWith(countPostData->{
                    assertEquals(2L, countPostData.count());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Mapping Boolean Expect MonoExistsPostDataResponse")
    void When_MappingBoolean_Expect_MonoExistsPostDataResponse(){
        Mono<ExistsPostDataResponse> existsPostDataResponseMono=postDataRestMapper.toExistsPostDataResponse(Boolean.TRUE);
        StepVerifier.create(existsPostDataResponseMono)
                .consumeNextWith(countPostData->{
                    assertEquals(true, countPostData.exists());
                })
                .verifyComplete();
    }
}
