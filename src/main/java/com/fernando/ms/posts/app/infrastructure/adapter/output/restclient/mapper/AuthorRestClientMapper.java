package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper;

import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response.UserResponse;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface AuthorRestClientMapper {

    default Flux<Author> toFluxAuthor(Flux<UserResponse> userResponseFlux){
        return userResponseFlux.map(this::toAuthor);
    }
    Author toAuthor(UserResponse userResponse);

    default Mono<Author> toMonoAuthor(Mono<UserResponse> userResponseMono) {
        return userResponseMono.map(this::toAuthor);
    }
}
