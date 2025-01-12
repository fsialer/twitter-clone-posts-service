package com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PostPersistenceMapper {
    default Flux<Post> toPosts(Flux<PostDocument> posts) {
        return posts.map(this::toPost); // Convierte cada elemento individualmente
    }

    default Mono<Post> toPost(Mono<PostDocument> post) {
        return post.map(this::toPost); // Convierte cada elemento individualmente
    }

    Post toPost(PostDocument post);

    @Mapping(target = "datePost",expression = "java(mapDatePost())")
    @Mapping(target = "createdAt",expression = "java(mapCreatedAt())")
    @Mapping(target = "updatedAt",expression = "java(mapUpdatedAt())")
    PostDocument toPostDocument(Post post);

    default LocalDateTime mapDatePost(){
        return LocalDateTime.now();
    }

    default LocalDateTime mapCreatedAt(){
        return LocalDateTime.now();
    }

    default LocalDateTime mapUpdatedAt(){
        return LocalDateTime.now();
    }

}
