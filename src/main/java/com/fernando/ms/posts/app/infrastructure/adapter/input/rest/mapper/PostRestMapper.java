package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.MediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostRestMapper {
    default Flux<PostResponse> toPostsResponse(Flux<Post> posts){
        return posts.map(this::toPostResponse);
    }

    PostResponse toPostResponse(Post post);

    default Post toPost(String userId,CreatePostRequest rq){
        return Post.builder()
                .content(rq.getContent())
                .datePost(rq.getDatePost())
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

    Post toPost(UpdatePostRequest rq);

    default ExistsPostResponse toExistsPostResponse(Boolean exists){
        return ExistsPostResponse.builder()
                .exists(exists)
                .build();
    }

    default Flux<PostAuthorResponse> toFluxPostAuthorResponse(Flux<Post> postFlux){
        return postFlux.map(this::toPostAuthorResponse);
    }

    default  PostAuthorResponse toPostAuthorResponse(Post post){
        return PostAuthorResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .datePost(post.getDatePost())
                .author( post.getAuthor().getFullName())
                .build();
    }

    default Mono<CountPostResponse> toCountPostResponse(Long count){
        return Mono.just(
                CountPostResponse.builder()
                        .count(count)
                        .build()
        );
    }

    default ExistsPostUserResponse toExistsPostUserResponse(Boolean exists){
        return ExistsPostUserResponse.builder()
                        .exists(exists)
                        .build();

    }



}
