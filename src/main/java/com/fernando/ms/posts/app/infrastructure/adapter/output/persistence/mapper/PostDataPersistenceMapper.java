package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface PostDataPersistenceMapper {
    default Mono<PostData> toPostData(Mono<PostDataDocument> postDataDocument){
        return postDataDocument.map(this::toPostData);
    }
    PostData toPostData(PostDataDocument postDataDocument);
    PostDataDocument toPostDataDocument(PostData postData);
}
