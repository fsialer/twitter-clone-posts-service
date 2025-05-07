package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.MediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostRestMapper {
    default Flux<PostResponse> toPostsResponse(Flux<Post> posts){
        return posts.map(this::toPostResponse);
    }

    PostResponse toPostResponse(Post post);

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

    Post toPost(UpdatePostRequest rq);

    default ExistsPostResponse toExistsPostResponse(Boolean exists){
        return ExistsPostResponse.builder()
                .exists(exists)
                .build();
    }

}
