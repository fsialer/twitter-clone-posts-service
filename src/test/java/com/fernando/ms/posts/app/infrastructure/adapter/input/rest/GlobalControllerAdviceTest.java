package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.application.ports.input.PostDataInputPort;
import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.input.PostMediaInputPort;
import com.fernando.ms.posts.app.domain.exceptions.*;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostDataRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostMediaRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
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

import static com.fernando.ms.posts.app.infrastructure.utils.ErrorCatalog.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {PostRestAdapter.class})
class GlobalControllerAdviceTest {
    @MockitoBean
    private PostRestMapper postRestMapper;

    @MockitoBean
    private PostInputPort postInputPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PostDataRestMapper postDataRestMapper;

    @MockitoBean
    private PostDataInputPort postDataInputPort;

    @MockitoBean
    private PostMediaRestMapper postMediaRestMapper;

    @MockitoBean
    private PostMediaInputPort postMediaInputPort;

    @Test
    @DisplayName("Expect PostNotFoundException When Post Identifier Is Invalid")
    void Expect_PostNotFoundException_When_UserIdentifierIsInvalid() {
        when(postInputPort.findById(anyString())).thenReturn(Mono.error(new PostNotFoundException()));

        webTestClient.get()
                .uri("/v1/posts/{id}","1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_NOT_FOUND.getCode());
                    assert response.getMessage().equals(POST_NOT_FOUND.getMessage());
                });
    }

    @Test
    @DisplayName("Expect InternalServerError When Exception Occurs")
    void Expect_InternalServerError_When_ExceptionOccurs() {
        when(postInputPort.findById(anyString())).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.get()
                .uri("/v1/posts/1")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_INTERNAL_SERVER_ERROR.getCode());
                    assert response.getMessage().equals(POST_INTERNAL_SERVER_ERROR.getMessage());
                });
    }

    @Test
    @DisplayName("Expect WebExchangeBindException When User Information Is Invalid")
    void Expect_WebExchangeBindException_When_UserInformationIsInvalid() throws JsonProcessingException {
        CreatePostRequest createUserRequest= CreatePostRequest.builder()
                .content("")
                .build();

        webTestClient.post()
                .uri("/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id","1")
                .bodyValue(objectMapper.writeValueAsString(createUserRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_BAD_PARAMETERS.getCode());
                    assert response.getMessage().equals(POST_BAD_PARAMETERS.getMessage());
                });
    }

    @Test
    @DisplayName("Expect PostRuleException When PostData Is Invalid")
    void Expect_PostRuleException_When_PostDataIsInvalid() throws JsonProcessingException {
        CreatePostDataRequest createPostDataRequest = TestUtilPostData.buildCreatePostDataRequestMock();
        when(postDataRestMapper.toPostData(any(String.class), any(CreatePostDataRequest.class)))
                .thenReturn(TestUtilPostData.buildPostDataMock());
        when(postDataInputPort.save(any())).thenReturn(Mono.error(new PostRuleException("PostData already exists")));
        webTestClient.post()
                .uri("/v1/posts/data")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id","1")
                .bodyValue(objectMapper.writeValueAsString(createPostDataRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_RULE_EXCEPTION.getCode());
                    assert response.getMessage().equals(POST_RULE_EXCEPTION.getMessage());
                });
    }

    @Test
    @DisplayName("Expect PostDataNotFoundException When PostData Identifier Is Invalid")
    void Expect_PostDataNotFoundException_When_PostDataIdentifierIsInvalid() {
        String postId = "postId123";
        when(postDataInputPort.delete(anyString(),anyString())).thenReturn(Mono.error(PostDataNotFoundException::new));
        webTestClient.delete()
                .uri("/v1/posts/data/{postId}", postId)
                .header("X-User-Id", "1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_DATA_NOT_FOUND.getCode());
                    assert response.getMessage().equals(POST_DATA_NOT_FOUND.getMessage());
                });

        Mockito.verify(postDataInputPort, times(1)).delete(anyString(),anyString());
    }

    @Test
    @DisplayName("Expect FallBackException When Service User Is Not Available")
    void Expect_FallBackException_When_ServiceUserIsNotAvailable() {
        when(postRestMapper.toFluxPostAuthorResponse(any(Flux.class))).thenReturn(Flux.empty());
        when(postInputPort.recent(anyString(), anyInt(), anyInt())).thenThrow(new UserFallBackException());

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/recent")
                        .queryParam("page","1")
                        .queryParam("size","25")
                        .build()
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(USERS_SERVICES_FAIL.getCode());
                    assert response.getMessage().equals(USERS_SERVICES_FAIL.getMessage());
                });

        Mockito.verify(postInputPort, times(1)).recent(anyString(),anyInt(),anyInt());
        Mockito.verify(postRestMapper, times(0)).toFluxPostAuthorResponse(any(Flux.class));
    }

    @Test
    @DisplayName("Expect AuthorNotFoundException When Service User Is Not Available")
    void Expect_AuthorNotFoundException_When_ServiceUserIsNotAvailable() {
        when(postRestMapper.toFluxPostAuthorResponse(any(Flux.class))).thenReturn(Flux.empty());
        when(postInputPort.me(anyString(), anyInt(), anyInt())).thenThrow(new AuthorNotFoundException());

        webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/v1/posts/me")
                        .queryParam("page","1")
                        .queryParam("size","25")
                        .build()
                )
                .header("X-User-Id","1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(AUTHOR_NOT_FOUND.getCode());
                    assert response.getMessage().equals(AUTHOR_NOT_FOUND.getMessage());
                });

        Mockito.verify(postInputPort, times(1)).me(anyString(),anyInt(),anyInt());
        Mockito.verify(postRestMapper, times(0)).toFluxPostAuthorResponse(any(Flux.class));
    }


}
