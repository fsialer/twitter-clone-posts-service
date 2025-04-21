package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.MediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostMedia;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostRestMapper {
    default Flux<PostResponse> toPostsResponse(Flux<Post> posts){
        return posts.map(this::toPostResponse);
    }

    default Mono<PostResponse> toPostResponse(Mono<Post> post){
        return post.map(this::toPostResponse);
    }

    PostResponse toPostResponse(Post post);

    //@Mapping(target = "user",expression = "java(mapUser(userId))")
    default Post toPost(String userId,CreatePostRequest rq){
        return Post.builder()
                .content(rq.getContent())
                .userId(userId)
                .media(mapMedia(rq.getMedia()))
                .build();
    }

    default Set<Media> mapMedia(Set<MediaRequest> mediaRequests) {
        return mediaRequests.stream()
                .map(this::toMedia)
                .collect(Collectors.toSet());
    }

    Media toMedia(MediaRequest mediaRequest);

    default Post mapUser(String userId){
        return Post.builder()
                .userId(userId)
                .build();
    }

    Post toPost(UpdatePostRequest rq);

    default ExistsPostResponse toExistsPostResponse(Boolean exists){
        return ExistsPostResponse.builder()
                .exists(exists)
                .build();
    }

//    default Flux<PostUserResponse> toPostsUserResponse(Flux<Post> posts){
//        return posts.map(this::toPostUserResponse);
//    }

//    @Mapping(target="user", expression = "java(toUserResponse(post))")
//    PostUserResponse toPostUserResponse(Post post);
//
//    default UserResponse toUserResponse(Post post){
//        return UserResponse.builder()
//                .id(post.getUser().getId())
//                .names(post.getUser().getNames())
//                .build();
//    }

}
