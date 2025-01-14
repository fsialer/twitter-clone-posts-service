package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(target = "user",expression = "java(mapUser(rq))")
    Post toPost(CreatePostRequest rq);

    default User mapUser(CreatePostRequest rq){
        return User.builder()
                .id(rq.getUserId())
                .build();
    }

    Post toPost(UpdatePostRequest rq);
}
