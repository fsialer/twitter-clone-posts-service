package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response.UserResponse;
import com.fernando.ms.posts.app.utils.TestUtilAuthor;
import com.fernando.ms.posts.app.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
class AuthorRestClientMapperTest {
    private AuthorRestClientMapper authorRestClientMapper;

    @BeforeEach
    void setUp(){
        authorRestClientMapper= Mappers.getMapper(AuthorRestClientMapper.class);
    }

    @Test
    @DisplayName("When Mapping FluxUserResponse Expect To FluxAuthor")
    void When_MappingFluxUserResponse_Expect_ToFluxAuthor(){
        UserResponse userResponse= TestUtilUser.buildUserResponseMock();
        Author author= TestUtilAuthor.buildAuthorMock();
        Flux<Author> fluxAuthor= authorRestClientMapper.toFluxAuthor(Flux.just(userResponse));
        StepVerifier.create(fluxAuthor)
                .consumeNextWith(author1 -> {
                    assertEquals(author1.getId(),author.getId());
                    assertEquals(author1.getNames(),author.getNames());
                    assertEquals(author1.getLastNames(),author.getLastNames());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Mapping UserResponse Expect To Author")
    void When_MappingUserResponse_Expect_ToAuthor(){
        UserResponse userResponse= TestUtilUser.buildUserResponseMock();
        Author author= TestUtilAuthor.buildAuthorMock();
        Author authorMapper= authorRestClientMapper.toAuthor(userResponse);
        assertEquals(authorMapper.getId(),author.getId());
        assertEquals(authorMapper.getNames(),author.getNames());
        assertEquals(authorMapper.getLastNames(),author.getLastNames());
    }

    @Test
    @DisplayName("When Mapping MonoUserResponse Expect To MonoAuthor")
    void When_MappingMonoUserResponse_Expect_ToMonoAuthor(){
        UserResponse userResponse= TestUtilUser.buildUserResponseMock();
        Author author= TestUtilAuthor.buildAuthorMock();
        Mono<Author> monoAuthor= authorRestClientMapper.toMonoAuthor(Mono.just(userResponse));
        StepVerifier.create(monoAuthor)
                .consumeNextWith(author1 -> {
                    assertEquals(author1.getId(),author.getId());
                    assertEquals(author1.getNames(),author.getNames());
                    assertEquals(author1.getLastNames(),author.getLastNames());
                })
                .verifyComplete();
    }


}
