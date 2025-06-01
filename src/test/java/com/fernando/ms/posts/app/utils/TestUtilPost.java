package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.MediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.*;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostMedia;

import java.time.LocalDateTime;
import java.util.Set;

public class TestUtilPost {

    public static Post buildPostMock(){
        return Post.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .userId("fdsfds4544")
                .media(Set.of(Media.builder()
                        .type("IMAGE")
                        .url("https://<storage>.blob.core.windows.net/posts/imagen.jpg")
                        .build()))
                .author(Author.builder()
                        .id("5d4d75sd4sd6sd")
                        .names("Fernando")
                        .lastNames("Sialer")
                        .build())
                .build();
    }

    public static PostDocument buildPostDocumentMock(){
        return PostDocument.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userId("fdsfds4544")
                .media(Set.of(PostMedia.builder()
                        .type("IMAGE")
                        .url("https://<storage>.blob.core.windows.net/posts/imagen.jpg")
                        .build()))
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
                .media(Set.of(MediaRequest.builder()
                                .type("IMAGE")
                                .url("https://<storage>.blob.core.windows.net/posts/imagen.jpg")
                        .build()))
                .datePost(LocalDateTime.now())
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

    public static PostAuthorResponse buildPostAuthorResponseMock(){
        return PostAuthorResponse.builder()
                .id("67894256c864356454574770")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .author("Fernando Alexis")
                .build();
    }

    public static CountPostResponse buildCountPostResponse(){
        return CountPostResponse.builder()
                .count(2L)
                .build();
    }

    public static CountPostDataResponse buildCountPostDataResponse(){
        return CountPostDataResponse.builder()
                .count(2L)
                .build();
    }




}
