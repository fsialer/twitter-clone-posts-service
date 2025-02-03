package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostUserResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.UserResponse;
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

    @Mapping(target = "user",expression = "java(mapUser(userId))")
    Post toPost(Long userId,CreatePostRequest rq);

    default User mapUser(Long userId){
        return User.builder()
                .id(userId)
                .build();
    }

    Post toPost(UpdatePostRequest rq);

    default ExistsPostResponse toExistsPostResponse(Boolean exists){
        return ExistsPostResponse.builder()
                .exists(exists)
                .build();
    }

    default Flux<PostUserResponse> toPostsUserResponse(Flux<Post> posts){
        return posts.map(this::toPostUserResponse);
    }

    @Mapping(target="user", expression = "java(toUserResponse(post))")
    PostUserResponse toPostUserResponse(Post post);

    default UserResponse toUserResponse(Post post){
        return UserResponse.builder()
                .id(post.getUser().getId())
                .names(post.getUser().getNames())
                .build();
    }

}
