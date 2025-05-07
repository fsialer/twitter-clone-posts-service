package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreateMediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostMediaResponse;
import com.fernando.ms.posts.app.utils.TestUtilPostMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostMediaRestMapperTest {
    private PostMediaRestMapper postMediaRestMapper;

    @BeforeEach
    void setUp(){
        postMediaRestMapper= Mappers.getMapper(PostMediaRestMapper.class);
    }

    @Test
    @DisplayName("When Mapping CreateMedia Have Data Expect List String With Url")
    void When_MappingCreateMediaHaveData_Expect_ListStringWithUrl(){
        CreateMediaRequest createMediaRequest= TestUtilPostMedia.builCreateMediaRequest();
        List<String> medias=postMediaRestMapper.toListString(createMediaRequest);
        assertEquals(createMediaRequest.getFilenames().getFirst(),medias.getFirst());
    }

    @Test
    @DisplayName("When Mapping FluxPostMedia Have Data Expect FluxPostMediaResponse With Url Correct")
    void When_MappingFluxPostMediaHaveData_Expect_FluxPostMediaResponseWithUrlCorrect(){
        PostMedia postMedia=TestUtilPostMedia.buildPostMedia();
        Flux<PostMediaResponse> fluxPostMediaResponse=postMediaRestMapper.toFluxPostMediaResponse(Flux.just(postMedia));
        StepVerifier.create(fluxPostMediaResponse)
                .consumeNextWith(postMediaResponse->{
                    assertEquals(postMediaResponse.blobUrl(), postMedia.getBlobUrl());
                    assertEquals(postMediaResponse.uploadUrl(), postMedia.getUploadUrl());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Mapping PostMedia Have Data Expect PostMediaResponse With Url Correct")
    void When_MappingPostMediaHaveData_Expect_PostMediaResponseWithUrlCorrect(){
        PostMedia postMedia=TestUtilPostMedia.buildPostMedia();
        PostMediaResponse fluxPostMediaResponse=postMediaRestMapper.toPostMediaResponse(postMedia);
        assertEquals(postMedia.getBlobUrl(),fluxPostMediaResponse.blobUrl());
        assertEquals(postMedia.getUploadUrl(),fluxPostMediaResponse.uploadUrl());
    }




}
