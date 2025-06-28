package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.application.ports.input.PostDataInputPort;
import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.input.PostMediaInputPort;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostDataRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostMediaRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreateMediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.*;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
import com.fernando.ms.posts.app.utils.TestUtilPostMedia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(PostRestAdapter.class)
class PostRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PostInputPort postInputPort;

    @MockitoBean
    private PostDataInputPort postDataInputPort;

    @MockitoBean
    private PostMediaInputPort postMediaInputPort;

    @MockitoBean
    private PostMediaRestMapper postMediaRestMapper;


    @MockitoBean
    private PostRestMapper postRestMapper;

    @MockitoBean
    private PostDataRestMapper postDataRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("When Users Are Exists Expect Users Information Return Successfully")
    void When_UsersAreExists_Expect_UsersInformationReturnSuccessfully() {
        PostResponse postResponse = TestUtilPost.buildPostResponseMock();
        Post post=TestUtilPost.buildPostMock();

        when(postInputPort.findAll()).thenReturn(Flux.just(post));
        when(postRestMapper.toPostsResponse(any(Flux.class))).thenReturn(Flux.just(postResponse));

        webTestClient.get()
                .uri("/v1/posts")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].content").isEqualTo("Hello everybody");
        Mockito.verify(postInputPort,times(1)).findAll();
        Mockito.verify(postRestMapper,times(1)).toPostsResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When Post Identifier Is Correct Expect Post Information Successfully")
    void When_PostIdentifierIsCorrect_Expect_PostInformationSuccessfully() {
        PostAuthorResponse postAuthorResponse = TestUtilPost.buildPostAuthorResponseMock();
        Post post = TestUtilPost.buildPostMock();
        when(postInputPort.findById(anyString())).thenReturn(Mono.just(post));
        when(postRestMapper.toPostAuthorResponse(any(Post.class))).thenReturn(postAuthorResponse);
        webTestClient.get()
                .uri("/v1/posts/{id}",1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isEqualTo("Hello everybody");

       Mockito.verify(postInputPort,times(1)).findById(anyString());
       Mockito.verify(postRestMapper,times(1)).toPostAuthorResponse(any(Post.class));
    }

    @Test
    @DisplayName("When Post Is Saved Successfully Expect Post Information Correct")
    void When_PostIsSavedSuccessfully_Expect_PostInformationCorrect() {
        CreatePostRequest createPostRequest = TestUtilPost.buildCreatePostRequestMock();
        Post post = TestUtilPost.buildPostMock();
        PostResponse postResponse = TestUtilPost.buildPostResponseMock();

        when(postRestMapper.toPost(anyString(),any(CreatePostRequest.class))).thenReturn(post);
        when(postInputPort.save(any(Post.class))).thenReturn(Mono.just(post));
        when(postRestMapper.toPostResponse(any(Post.class))).thenReturn(postResponse);

        webTestClient.post()
                .uri("/v1/posts")
                .header("X-User-Id","1")
                .bodyValue(createPostRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.content").isEqualTo("Hello everybody");

        Mockito.verify(postRestMapper, times(1)).toPost(anyString(),any(CreatePostRequest.class));
        Mockito.verify(postInputPort, times(1)).save(any(Post.class));
        Mockito.verify(postRestMapper, times(1)).toPostResponse(any(Post.class));
    }

    @Test
    @DisplayName("When Post Information Is Correct Expect Update Post Successfully")
    void When_PostInformationIsCorrect_Expect_UpdatePostSuccessfully() throws JsonProcessingException {
        UpdatePostRequest updatePostRequest=TestUtilPost.buildUpdatePostRequestMock();
        Post post=TestUtilPost.buildPostMock();
        PostResponse postResponse=TestUtilPost.buildPostResponseMock();
        when(postRestMapper.toPost(any(UpdatePostRequest.class))).thenReturn(post);
        when(postInputPort.update(anyString(),any(Post.class))).thenReturn(Mono.just(post));
        when(postRestMapper.toPostResponse(any(Post.class))).thenReturn(postResponse);

        webTestClient.put()
                .uri("/v1/posts/{id}","1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(updatePostRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isEqualTo("Hello everybody");
    }

    @Test
    @DisplayName("When Post Exists Expect Post Deleted Successfully")
    void When_PostExists_Expect_PostDeletedSuccessfully() {
        when(postInputPort.delete(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/v1/posts/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(postInputPort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("When Post Verification Is Successful Expect Post Verified")
    void When_PostVerificationIsSuccessful_Expect_PostVerified() {
        ExistsPostResponse existsPostResponse = TestUtilPost.buildExistsPostResponseMock();

        when(postInputPort.verify(anyString())).thenReturn(Mono.just(true));
        when(postRestMapper.toExistsPostResponse(anyBoolean())).thenReturn(existsPostResponse);

        webTestClient.get()
                .uri("/v1/posts/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(true);

        Mockito.verify(postInputPort, times(1)).verify(anyString());
        Mockito.verify(postRestMapper, times(1)).toExistsPostResponse(anyBoolean());
    }

    @Test
    @DisplayName("When Post Verification Is Incorrect Expect Post Do Not Verified")
    void When_PostVerificationIsIncorrect_Expect_PostDoNotVerified() {
        ExistsPostResponse existsPostResponse = TestUtilPost.buildExistsPostResponseMock();
        existsPostResponse.setExists(false);
        when(postInputPort.verify(anyString())).thenReturn(Mono.just(false));
        when(postRestMapper.toExistsPostResponse(anyBoolean())).thenReturn(existsPostResponse);

        webTestClient.get()
                .uri("/v1/posts/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(false);

        Mockito.verify(postInputPort, times(1)).verify(anyString());
        Mockito.verify(postRestMapper, times(1)).toExistsPostResponse(anyBoolean());
    }

    @Test
    @DisplayName("when PostData Is Valid Expect Save Data Successfully")
    void when_PostDataIsValid_Expect_SaveDataSuccessfully() {
        CreatePostDataRequest createPostDataRequest = TestUtilPostData.buildCreatePostDataRequestMock();
        when(postDataRestMapper.toPostData(any(String.class), any(CreatePostDataRequest.class)))
                .thenReturn(TestUtilPostData.buildPostDataMock());
        when(postDataInputPort.save(any())).thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/v1/posts/data")
                .header("X-User-Id", "1")
                .bodyValue(createPostDataRequest)
                .exchange()
                .expectStatus().isNoContent();
        Mockito.verify(postDataRestMapper, times(1))
                .toPostData(any(String.class), any(CreatePostDataRequest.class));
        Mockito.verify(postDataInputPort, times(1)).save(any());
    }


    @Test
    @DisplayName("When Delete Data By Id Expect Complete Successfully")
    void When_DeleteDataById_Expect_CompleteSuccessfully() {
        String postDataId = "postDataId123";
        when(postDataInputPort.delete(anyString(),anyString())).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/v1/posts/data/{id}", postDataId)
                .header("X-User-Id", "1")
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(postDataInputPort, times(1)).delete(anyString(),anyString());
    }


    @Test
    @DisplayName("When Generate SasUrl Expect Valid SasUrls")
    void When_GenerateSasUrl_Expect_ValidSasUrls() {
        PostMedia postMedia = TestUtilPostMedia.buildPostMedia();
        PostMediaResponse postMediaResponse = TestUtilPostMedia.buildPostMediaResponse();
        CreateMediaRequest createMediaRequest = TestUtilPostMedia.builCreateMediaRequest();

        when(postMediaInputPort.generateSasUrl(anyList())).thenReturn(Flux.just(postMedia));
        when(postMediaRestMapper.toFluxPostMediaResponse(any(Flux.class))).thenReturn(Flux.just(postMediaResponse));

        webTestClient.post()
                .uri("/v1/posts/media")
                .bodyValue(createMediaRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].blobUrl").isEqualTo(postMedia.getBlobUrl())
                .jsonPath("$[0].uploadUrl").isEqualTo(postMedia.getUploadUrl());

        Mockito.verify(postMediaInputPort, times(1)).generateSasUrl(anyList());
        Mockito.verify(postMediaRestMapper, times(1)).toFluxPostMediaResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When UserId Is Valid Expect List All Post Recents")
    void When_UserIdIsValid_Expect_ListAllPostRecents() {
        PostAuthorResponse postAuthorResponse = TestUtilPost.buildPostAuthorResponseMock();
        Post post=TestUtilPost.buildPostMock();

        when(postInputPort.recent(anyString(),anyInt(),anyInt())).thenReturn(Flux.just(post));
        when(postRestMapper.toFluxPostAuthorResponse(any(Flux.class))).thenReturn(Flux.just(postAuthorResponse));

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/recent")
                        .queryParam("page","1")
                        .queryParam("size","25")
                        .build()
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(postAuthorResponse.id())
                .jsonPath("$[0].content").isEqualTo(postAuthorResponse.content());

        Mockito.verify(postInputPort, times(1)).recent(anyString(),anyInt(),anyInt());
        Mockito.verify(postRestMapper, times(1)).toFluxPostAuthorResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When UserId Is Valid Expect List All Post Me")
    void When_UserIdIsValid_Expect_ListAllPostMe() {
        PostAuthorResponse postAuthorResponse = TestUtilPost.buildPostAuthorResponseMock();
        Post post=TestUtilPost.buildPostMock();

        when(postInputPort.me(anyString(),anyInt(),anyInt())).thenReturn(Flux.just(post));
        when(postRestMapper.toFluxPostAuthorResponse(any(Flux.class))).thenReturn(Flux.just(postAuthorResponse));

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/me")
                        .queryParam("page","1")
                        .queryParam("size","25")
                        .build()
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(postAuthorResponse.id())
                .jsonPath("$[0].content").isEqualTo(postAuthorResponse.content());

        Mockito.verify(postInputPort, times(1)).me(anyString(),anyInt(),anyInt());
        Mockito.verify(postRestMapper, times(1)).toFluxPostAuthorResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When UserId Is Valid Expect Count Post By UserId")
    void When_UserIdIsValid_Expect_CountPostByUserId() {
        CountPostResponse countPostResponse = TestUtilPost.buildCountPostResponse();

        when(postInputPort.countPostByUser(anyString())).thenReturn(Mono.just(2L));
        when(postRestMapper.toCountPostResponse(anyLong())).thenReturn(Mono.just(countPostResponse));

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/count")
                        .build()
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.count").isEqualTo(countPostResponse.count());

        Mockito.verify(postInputPort, times(1)).countPostByUser(anyString());
        Mockito.verify(postRestMapper, times(1)).toCountPostResponse(anyLong());
    }

    @Test
    @DisplayName("When PostId Is Valid Expect Count PostData By PostId")
    void When_PostIdIsValid_Expect_CountPostDataByPostId() {
        CountPostDataResponse countPostDataResponse = TestUtilPost.buildCountPostDataResponse();

        when(postDataInputPort.countPostDataByPost(anyString())).thenReturn(Mono.just(2L));
        when(postDataRestMapper.toCountPostDataResponse(anyLong())).thenReturn(Mono.just(countPostDataResponse));

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/data/count/${postId}")
                        .build("1")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.count").isEqualTo(countPostDataResponse.count());

        Mockito.verify(postDataInputPort, times(1)).countPostDataByPost(anyString());
        Mockito.verify(postDataRestMapper, times(1)).toCountPostDataResponse(anyLong());
    }

    @Test
    @DisplayName("When PostId And UserId Are Valid Expect True")
    void When_PostIdAndUserIdAreValid_Expect_True() {
        ExistsPostDataResponse existsPostDataResponse = TestUtilPost.buildExistsPostDataResponseMock();

        when(postDataInputPort.verifyExistsPostData(anyString(),anyString())).thenReturn(Mono.just(Boolean.TRUE));
        when(postDataRestMapper.toExistsPostDataResponse(anyBoolean())).thenReturn(Mono.just(existsPostDataResponse));

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/data/{postId}/exists")
                        .build("1")
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(existsPostDataResponse.exists());

        Mockito.verify(postDataInputPort, times(1)).verifyExistsPostData(anyString(),anyString());
        Mockito.verify(postDataRestMapper, times(1)).toExistsPostDataResponse(anyBoolean());
    }

    @Test
    @DisplayName("When PostId And UserId Is Valid Expect True For Post")
    void When_PostIdAndUserIdAreValid_Expect_TrueForPost() {
        ExistsPostUserResponse existsPostUserResponse = TestUtilPost.buildExistsPostUserResponseMock();

        when(postInputPort.verifyPostByUserId(anyString(),anyString())).thenReturn(Mono.just(Boolean.TRUE));
        when(postRestMapper.toExistsPostUserResponse(anyBoolean())).thenReturn(existsPostUserResponse);

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/{postId}/user")
                        .build("1")
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(existsPostUserResponse.exists());

        Mockito.verify(postInputPort, times(1)).verifyPostByUserId(anyString(),anyString());
        Mockito.verify(postRestMapper, times(1)).toExistsPostUserResponse(anyBoolean());
    }
}
