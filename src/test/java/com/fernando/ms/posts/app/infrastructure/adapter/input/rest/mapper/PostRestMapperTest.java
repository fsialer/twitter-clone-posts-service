package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.MediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostAuthorResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostRestMapperTest {
    private PostRestMapper postRestMapper;

    @BeforeEach
    void setUp(){
        postRestMapper= Mappers.getMapper(PostRestMapper.class);
    }

    @Test
    @DisplayName("When Mapping FluxPost Have Data Expect FluxPostResponse Correct")
    void When_MappingFluxPostHaveData_Expect_FluxPostResponseCorrect(){
        Post post= TestUtilPost.buildPostMock();
        Flux<PostResponse> fluxPostResponse=postRestMapper.toPostsResponse(Flux.just(post));
        StepVerifier.create(fluxPostResponse)
                .consumeNextWith(postResponse->{
                    assertEquals(postResponse.getDatePost(), post.getDatePost());
                    assertEquals(postResponse.getId(), post.getId());
                    assertEquals(postResponse.getContent(), post.getContent());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Mapping Post Have Data Expect PostResponse Correct")
    void When_MappingPostHaveData_Expect_PostResponseCorrect(){
        Post post= TestUtilPost.buildPostMock();
        PostResponse postResponse=postRestMapper.toPostResponse(post);

        assertEquals(postResponse.getDatePost(), post.getDatePost());
        assertEquals(postResponse.getId(), post.getId());
        assertEquals(postResponse.getContent(), post.getContent());
    }

    @Test
    @DisplayName("When Mapping UserId And CreatePostRequest Have Data Expect Post Correct")
    void When_MappingUserIdAndCreatePostRequestHaveData_Expect_PostCorrect(){
        String userId="47sd556d75sd";
        CreatePostRequest createPostRequest=TestUtilPost.buildCreatePostRequestMock();
        Post post=postRestMapper.toPost(userId,createPostRequest);
        assertEquals(userId, post.getUserId());
        assertEquals( createPostRequest.getContent(),post.getContent());
    }

    @Test
    @DisplayName("When Mapping Set Media Request Expect Set Media")
    void When_MappingSetMediaRequest_Expect_SetMedia(){
        CreatePostRequest createPostRequest=TestUtilPost.buildCreatePostRequestMock();
        Set<MediaRequest> setMediaRequest=createPostRequest.getMedia();
        Set<Media> setMedia=postRestMapper.mapMedia(setMediaRequest);
        assertEquals(setMediaRequest.iterator().next().getType(),setMedia.iterator().next().getType());
        assertEquals(setMediaRequest.iterator().next().getUrl(),setMedia.iterator().next().getUrl());
    }

    @Test
    @DisplayName("When Mapping UpdatePostRequest Expect Post")
    void When_MappingUpdatePostRequest_Expect_Post(){
        UpdatePostRequest updatePostRequest=TestUtilPost.buildUpdatePostRequestMock();
        Post post=postRestMapper.toPost(updatePostRequest);
        assertEquals(updatePostRequest.getContent(),post.getContent());
    }

    @Test
    @DisplayName("When Mapping Boolean Expect ExistsPostResponse")
    void When_MappingBoolean_Expect_ExistsPostResponse(){
        ExistsPostResponse existsPostResponse=postRestMapper.toExistsPostResponse(Boolean.TRUE);
        assertTrue(existsPostResponse.getExists());
    }

    @Test
    @DisplayName("When Mapping PostFlux Expect FluxPostAuthorResponse")
    void When_MappingPostFlux_Expect_FluxPostAuthorResponse(){
        Post post=TestUtilPost.buildPostMock();
        Flux<PostAuthorResponse> fluxPostAuthorResponse=postRestMapper.toFluxPostAuthorResponse(Flux.just(post));
        StepVerifier.create(fluxPostAuthorResponse)
                        .consumeNextWith(postAuthor->{
                            assertEquals(postAuthor.id(),post.getId());
                            assertEquals(postAuthor.content(),post.getContent());
                            assertEquals(postAuthor.author(),post.getAuthor().getNames().concat(" ").concat(post.getAuthor().getLastNames()==null?"":post.getAuthor().getLastNames()).trim());
                        })
                .verifyComplete();

    }

    @Test
    @DisplayName("When Mapping Post Expect PostAuthorResponse")
    void When_MappingPost_Expect_ostAuthorResponse(){
        Post post=TestUtilPost.buildPostMock();
        post.getAuthor().setLastNames(null);
        PostAuthorResponse postAuthorResponse=postRestMapper.toPostAuthorResponse(post);
        assertEquals(postAuthorResponse.id(),post.getId());
        assertEquals(postAuthorResponse.content(),post.getContent());
        assertEquals(postAuthorResponse.author(),post.getAuthor().getNames().concat(" ").concat(post.getAuthor().getLastNames()==null?"":post.getAuthor().getLastNames()).trim());


    }
}
