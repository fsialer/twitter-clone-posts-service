package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.CountPostDataResponse;
import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface PostDataRestMapper {

    default PostData toPostData(String userId,CreatePostDataRequest createPostDataRequest){
        return PostData.builder()
                .postId(createPostDataRequest.getPostId())
                .typeTarget(createPostDataRequest.getTypeTarget())
                .userId(userId)
                .build();
    }

    default Mono<CountPostDataResponse> toCountPostDataResponse(Long count){
        return Mono.just(
                CountPostDataResponse.builder()
                        .count(count)
                        .build()
        );
    }
}
