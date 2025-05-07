package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
}
