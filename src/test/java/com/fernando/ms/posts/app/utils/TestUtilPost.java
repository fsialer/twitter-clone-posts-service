package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostUserResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;

import java.time.LocalDateTime;

public class TestUtilPost {

    public static Post buildPostMock(){
        return Post.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .user(User.builder().id(1L).build())
                .build();
    }

    public static PostDocument buildPostDocumentMock(){
        return PostDocument.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .postUser(PostUser.builder().userId(1L).build())
                .build();
    }

    public static PostResponse buildPostResponseMock(){
        return PostResponse.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .build();
    }

    public static CreatePostRequest buildCreatePostRequestMock(){
        return CreatePostRequest.builder()
                .content("Hello everybody")
                .build();
    }

    public static UpdatePostRequest buildUpdatePostRequestMock(){
        return UpdatePostRequest.builder()
                .content("Hello everybody")
                .build();
    }

    public static ExistsPostResponse buildExistsPostResponseMock(){
        return ExistsPostResponse.builder()
                .exists(true)
                .build();
    }

    public static PostUserResponse buildPostUserResponseMock(){
        return PostUserResponse.builder()
                .id("67894256c864356454574770")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .user(UserResponse.builder()
                        .id(1L)
                        .names("Fernando Sialer")
                        .build())
                .build();
    }

    public static PostUser buildPostUserMock(){
        return PostUser.builder()
                .userId(1L)
                .build();
    }




}
