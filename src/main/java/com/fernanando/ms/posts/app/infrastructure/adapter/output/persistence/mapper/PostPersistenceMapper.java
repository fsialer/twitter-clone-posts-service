package com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface PostPersistenceMapper {
    default Flux<Post> toPosts(Flux<PostDocument> posts) {
        return posts.map(this::toPost); // Convierte cada elemento individualmente
    }

    Post toPost(PostDocument post);
}
