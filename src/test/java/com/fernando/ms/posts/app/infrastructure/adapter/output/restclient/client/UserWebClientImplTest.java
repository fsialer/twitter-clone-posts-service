package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.posts.app.domain.exceptions.UserFallBackException;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.impl.UserWebClientImpl;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response.UserResponse;
import com.fernando.ms.posts.app.infrastructure.config.ServiceProperties;
import com.fernando.ms.posts.app.utils.TestUtilUser;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWebClientImplTest {
    @Mock
    private WebClient webClient;

    private UserWebClientImpl userWebClientImpl;

    @Mock
    private ServiceProperties serviceProperties;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Test
    @DisplayName("When Service User Followed Is Available Expect ListUsers")
    void When_ServiceUserFollowedIsAvailable_Expect_ListUsers(){
        String followerId = "123";
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        Mockito.<WebClient.RequestHeadersUriSpec<?>>when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersUriSpec.uri("/{id}/followed", followerId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.just(userResponse));
        when(serviceProperties.getUsersService()).thenReturn("http://fake-url");

        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        userWebClientImpl = new UserWebClientImpl(circuitBreakerRegistry, webClientBuilder, serviceProperties);

        StepVerifier.create(userWebClientImpl.findFollowedByFollowerId(followerId))
                .consumeNextWith(user->{
                    assertEquals(user.id(),userResponse.id());
                    assertEquals(user.names(),userResponse.names());
                    assertEquals(user.lastNames(),userResponse.lastNames());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Expect FallBackException When Service User Followed Is Not Available")
    void Expect_FallBackException_When_ServiceUserFollowedIsNotAvailable(){
        String followerId = "123";
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        Mockito.<WebClient.RequestHeadersUriSpec<?>>when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersUriSpec.uri("/{id}/followed", followerId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.error(new UserFallBackException()));
        when(serviceProperties.getUsersService()).thenReturn("http://fake-url");

        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        userWebClientImpl = new UserWebClientImpl(circuitBreakerRegistry, webClientBuilder, serviceProperties);

        StepVerifier.create(userWebClientImpl.findFollowedByFollowerId(followerId))
                .expectError(UserFallBackException.class)
                .verify();
    }

    @Test
    @DisplayName("When Service User Find Me Is Available Expect An User")
    void When_ServiceUserFindMe_Expect_AnUser(){
        String userId = "123";
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        Mockito.<WebClient.RequestHeadersUriSpec<?>>when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersUriSpec.uri("/me")).thenReturn(requestHeadersSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersSpec.header("X-User-Id", userId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(userResponse));
        when(serviceProperties.getUsersService()).thenReturn("http://fake-url");

        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        userWebClientImpl = new UserWebClientImpl(circuitBreakerRegistry, webClientBuilder, serviceProperties);

        Mono<UserResponse> result = userWebClientImpl.findByUserId(userId);
        StepVerifier.create(result)
                .consumeNextWith(user->{
                    assertEquals(user.id(),userResponse.id());
                    assertEquals(user.names(),userResponse.names());
                    assertEquals(user.lastNames(),userResponse.lastNames());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Expect FallBackException When Service User Find Me Is Not Available")
    void Expect_FallBackException_When_ServiceUserFindMeIsNotAvailable(){
        String userId = "123";
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        Mockito.<WebClient.RequestHeadersUriSpec<?>>when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersUriSpec.uri("/me")).thenReturn(requestHeadersSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersSpec.header("X-User-Id", userId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.error(new UserFallBackException()));
        when(serviceProperties.getUsersService()).thenReturn("http://fake-url");

        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        userWebClientImpl = new UserWebClientImpl(circuitBreakerRegistry, webClientBuilder, serviceProperties);

        StepVerifier.create(userWebClientImpl.findByUserId(userId))
                .expectError(UserFallBackException.class)
                .verify();
    }
}
