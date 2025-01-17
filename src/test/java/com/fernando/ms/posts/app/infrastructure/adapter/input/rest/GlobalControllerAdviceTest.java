package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.fernando.ms.posts.app.infrastructure.utils.ErrorCatalog.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {PostRestAdapter.class})
public class GlobalControllerAdviceTest {
    @MockBean
    private PostRestMapper postRestMapper;

    @MockBean
    private PostInputPort postInputPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Expect PostNotFoundException When Post Identifier Is Invalid")
    void Expect_PostNotFoundException_When_UserIdentifierIsInvalid() {
        //when(postRestAdapter.findById(anyString())).thenReturn(Mono.error(new PostNotFoundException()));
        when(postInputPort.findById(anyString())).thenReturn(Mono.error(new PostNotFoundException()));

        webTestClient.get()
                .uri("/posts/{id}","1")
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
                .uri("/posts/1")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(INTERNAL_SERVER_ERROR.getCode());
                    assert response.getMessage().equals(INTERNAL_SERVER_ERROR.getMessage());
                });
    }

    @Test
    @DisplayName("Expect WebExchangeBindException When User Information Is Invalid")
    void Expect_WebExchangeBindException_When_UserInformationIsInvalid() throws JsonProcessingException {
        CreatePostRequest createUserRequest= CreatePostRequest.builder()
                .content("")
                .userId(1L)
                .build();

        webTestClient.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createUserRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_BAD_PARAMETERS.getCode());
                    assert response.getMessage().equals(POST_BAD_PARAMETERS.getMessage());
                });
    }


}
