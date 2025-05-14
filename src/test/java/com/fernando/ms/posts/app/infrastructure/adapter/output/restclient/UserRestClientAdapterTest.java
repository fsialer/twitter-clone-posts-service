package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.AuthorRestClientMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response.UserResponse;
import com.fernando.ms.posts.app.utils.TestUtilAuthor;
import com.fernando.ms.posts.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestClientAdapterTest {

    @Mock
    private UserWebClient userWebClient;

    @InjectMocks
    private UserRestClientAdapter userRestClientAdapter;

    @Mock
    private AuthorRestClientMapper authorRestClientMapper;

    @Test
    @DisplayName("When Service User Is Available Expect AListAuthors")
    void When_ServiceUserIsAvailable_Expect_AListAuthors(){
        UserResponse userResponse=TestUtilUser.buildUserResponseMock();
        Author authorMock= TestUtilAuthor.buildAuthorMock();
        when(userWebClient.findFollowedByFollowerId(anyString())).thenReturn(Flux.just(userResponse));
        when(authorRestClientMapper.toFluxAuthor(any(Flux.class))).thenReturn(Flux.just(authorMock));

        Flux<Author> authorFlux=userRestClientAdapter.findAuthorByUserId("556621d65s26d");

        StepVerifier.create(authorFlux)
                .consumeNextWith(author -> {
                    assertEquals(author.getId(),authorMock.getId());
                    assertEquals(author.getNames(),authorMock.getNames());
                    assertEquals(author.getLastNames(),authorMock.getLastNames());
                })
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).findFollowedByFollowerId(anyString());
        Mockito.verify(authorRestClientMapper,times(1)).toFluxAuthor(any(Flux.class));
    }

    @Test
    @DisplayName("When Service User Is Available Expect An User")
    void When_ServiceUserIsAvailable_Expect_AnUser(){
        UserResponse userResponse=TestUtilUser.buildUserResponseMock();
        Author authorMock= TestUtilAuthor.buildAuthorMock();
        when(userWebClient.findByUserId(anyString())).thenReturn(Mono.just(userResponse));
        when(authorRestClientMapper.toMonoAuthor(any(Mono.class))).thenReturn(Mono.just(authorMock));

        Mono<Author> authorMono=userRestClientAdapter.me("556621d65s26d");

        StepVerifier.create(authorMono)
                .consumeNextWith(author -> {
                    assertEquals(author.getId(),authorMock.getId());
                    assertEquals(author.getNames(),authorMock.getNames());
                    assertEquals(author.getLastNames(),authorMock.getLastNames());
                })
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).findByUserId(anyString());
        Mockito.verify(authorRestClientMapper,times(1)).toMonoAuthor(any(Mono.class));
    }


}
