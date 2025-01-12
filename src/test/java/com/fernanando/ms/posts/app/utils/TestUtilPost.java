package com.fernanando.ms.posts.app.utils;

import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestUtilPost {

    public static Post buildPostMock(){
        return Post.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .build();
    }

    public static PostDocument buildPostDocumentMock(){
        return PostDocument.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static PostResponse buildPostResponseMock(){
        return PostResponse.builder()
                .id("1")
                .content("Hello everybody")
                .datePost(LocalDateTime.now())
                .build();
    }

}
