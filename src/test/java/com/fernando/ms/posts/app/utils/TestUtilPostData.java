package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;

public class TestUtilPostData {
    public static PostData buildPostDataMock(){
        return PostData.builder()
                .id("1")
                .postId("68045526dffe6e2de223e55b")
                .typeTarget("LIKE")
                .userId("fdsfds4544")
                .build();
    }

    public static PostDataDocument buildPostDataDocumentMock(){
        return PostDataDocument.builder()
                .id("1")
                .postId("68045526dffe6e2de223e55b")
                .typeTarget("LIKE")
                .userId("fdsfds4544")
                .build();
    }

    public static CreatePostDataRequest buildCreatePostDataRequestMock(){
        return CreatePostDataRequest.builder()
                .postId("68045526dffe6e2de223e55b")
                .typeTarget("LIKE")
                .build();
    }
}
