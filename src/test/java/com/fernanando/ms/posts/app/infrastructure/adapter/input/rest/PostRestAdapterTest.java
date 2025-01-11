package com.fernanando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernanando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernanando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
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

}
