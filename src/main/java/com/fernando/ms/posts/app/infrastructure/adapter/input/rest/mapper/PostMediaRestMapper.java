package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreateMediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostMediaResponse;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMediaRestMapper {
    default List<String> toListString(CreateMediaRequest rq){
        return rq.getFilenames();
    }

    default Flux<PostMediaResponse> toFluxPostMediaResponse(Flux<PostMedia> postMedia){
        return postMedia.map(this::toPostMediaResponse);
    }

    PostMediaResponse toPostMediaResponse(PostMedia postMedia);
}
