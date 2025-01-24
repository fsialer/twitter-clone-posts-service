package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostUserResponse;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(PostRestAdapter.class)
public class PostRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PostInputPort postInputPort;

    @MockBean
    private PostRestMapper postRestMapper;

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
                .uri("/posts")
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
        PostResponse postResponse = TestUtilPost.buildPostResponseMock();
        Post post = TestUtilPost.buildPostMock();
        when(postInputPort.findById(anyString())).thenReturn(Mono.just(post));
        when(postRestMapper.toPostResponse(any(Post.class))).thenReturn(postResponse);
        webTestClient.get()
                .uri("/posts/{id}",1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isEqualTo("Hello everybody");

       Mockito.verify(postInputPort,times(1)).findById(anyString());
        Mockito.verify(postRestMapper,times(1)).toPostResponse(any(Post.class));
    }

    @Test
    @DisplayName("When Post Is Saved Successfully Expect Post Information Correct")
    void When_PostIsSavedSuccessfully_Expect_PostInformationCorrect() {
        CreatePostRequest createPostRequest = TestUtilPost.buildCreatePostRequestMock();
        Post post = TestUtilPost.buildPostMock();
        PostResponse postResponse = TestUtilPost.buildPostResponseMock();

        when(postRestMapper.toPost(any(CreatePostRequest.class))).thenReturn(post);
        when(postInputPort.save(any(Post.class))).thenReturn(Mono.just(post));
        when(postRestMapper.toPostResponse(any(Post.class))).thenReturn(postResponse);

        webTestClient.post()
                .uri("/posts")
                .bodyValue(createPostRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.content").isEqualTo("Hello everybody");

        Mockito.verify(postRestMapper, times(1)).toPost(any(CreatePostRequest.class));
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
                .uri("/posts/{id}","1")
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
                .uri("/posts/{id}", 1L)
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
                .uri("/posts/{id}/verify", 1L)
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
                .uri("/posts/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(false);

        Mockito.verify(postInputPort, times(1)).verify(anyString());
        Mockito.verify(postRestMapper, times(1)).toExistsPostResponse(anyBoolean());
    }

    @Test
    @DisplayName("When User Posts Exist Expect List of User Posts")
    void When_UserPostsExist_Expect_ListOfUserPosts() {
        PostUserResponse postUserResponse = TestUtilPost.buildPostUserResponseMock();

        when(postInputPort.findAllPostMe(anyLong(), anyLong(), anyLong())).thenReturn(Flux.just(TestUtilPost.buildPostMock()));
        when(postRestMapper.toPostsUserResponse(any(Flux.class))).thenReturn(Flux.just(postUserResponse));

        webTestClient.get()
                .uri("/posts/{userId}/me?size=10&page=0", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].content").isEqualTo("Hello everybody");

        Mockito.verify(postInputPort, Mockito.times(1)).findAllPostMe(anyLong(), anyLong(), anyLong());
        Mockito.verify(postRestMapper, Mockito.times(1)).toPostsUserResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When Follower Id Is Provided Expect List Of Recent Posts")
    void When_FollowerIdIsProvided_Expect_ListOfRecentPosts() {
        PostUserResponse postUserResponse = TestUtilPost.buildPostUserResponseMock();
        Post post = TestUtilPost.buildPostMock();

        when(postInputPort.findAllPostRecent(anyLong(), anyLong(), anyLong())).thenReturn(Flux.just(post));
        when(postRestMapper.toPostsUserResponse(any(Flux.class))).thenReturn(Flux.just(postUserResponse));

        webTestClient.get()
                .uri("/posts/{follower}/recent?size=10&page=0", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].content").isEqualTo("Hello everybody");

        Mockito.verify(postInputPort, times(1)).findAllPostRecent(anyLong(), anyLong(), anyLong());
        Mockito.verify(postRestMapper, times(1)).toPostsUserResponse(any(Flux.class));
    }


}
