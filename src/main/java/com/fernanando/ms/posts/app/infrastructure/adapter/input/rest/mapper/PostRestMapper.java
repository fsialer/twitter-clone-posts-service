package com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface PostRestMapper {
    default Flux<PostResponse> toPostsResponse(Flux<Post> posts){
        return posts.map(this::toPostResponse);
    }

    default Mono<PostResponse> toPostResponse(Mono<Post> post){
        return post.map(this::toPostResponse);
    }

    PostResponse toPostResponse(Post post);
}
