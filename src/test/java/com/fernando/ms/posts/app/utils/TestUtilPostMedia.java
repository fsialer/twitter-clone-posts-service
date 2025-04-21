package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreateMediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostMediaResponse;

import java.util.List;

public class TestUtilPostMedia {

    public static PostMedia buildPostMedia(){
        return PostMedia.builder()
                .uploadUrl("https://fernando-ms-posts.blob.core.windows.net/media/2023-10-01/test_image.jpg?sv=2023-10-01T00%3A00%3A00Z&se=2023-10-01T00%3A00%3A00Z&sr=b&sp=racwdl&sig=signature")
                .blobUrl("https://fernando-ms-posts.blob.core.windows.net/media/2023-10-01/test_image.jpg")
                .build();
    }

    public static PostMediaResponse buildPostMediaResponse(){
        return PostMediaResponse.builder()
                .uploadUrl("https://fernando-ms-posts.blob.core.windows.net/media/2023-10-01/test_image.jpg?sv=2023-10-01T00%3A00%3A00Z&se=2023-10-01T00%3A00%3A00Z&sr=b&sp=racwdl&sig=signature")
                .blobUrl("https://fernando-ms-posts.blob.core.windows.net/media/2023-10-01/test_image.jpg")
                .build();
    }

    public static CreateMediaRequest builCreateMediaRequest(){
        return CreateMediaRequest.builder()
                .filenames(List.of("test_image.jpg"))
                .build();
    }
}
