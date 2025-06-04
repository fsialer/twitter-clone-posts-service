package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostDataPersistenceMapper {
    default Mono<PostData> toPostData(Mono<PostDataDocument> postDataDocument){
        return postDataDocument.map(this::toPostData);
    }
    PostData toPostData(PostDataDocument postDataDocument);
    PostDataDocument toPostDataDocument(PostData postData);
}