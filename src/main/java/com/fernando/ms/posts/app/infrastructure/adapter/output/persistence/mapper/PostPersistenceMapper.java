package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostMedia;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostPersistenceMapper {
    default Flux<Post> toPosts(Flux<PostDocument> posts) {
        return posts.map(this::toPost);
    }

    default Mono<Post> toPost(Mono<PostDocument> post) {
        return post.map(this::toPost);
    }

    Post toPost(PostDocument post);

    default PostDocument toPostDocument(Post post){
        return PostDocument.builder()
                .id(post.getId())
                .content(post.getContent())
                .datePost(post.getDatePost())
                .userId(post.getUserId())
                .createdAt(mapCreatedAt())
                .updatedAt(mapUpdatedAt())
                .media(mapMedia(post.getMedia()))
                .build();
    }

    default Set<PostMedia> mapMedia(Set<Media> media) {
        return media.stream()
                .map(this::toPostMedia)
                .collect(Collectors.toSet());
    }

    PostMedia toPostMedia(Media media);

    default LocalDateTime mapCreatedAt(){
        return LocalDateTime.now();
    }

    default LocalDateTime mapUpdatedAt(){
        return LocalDateTime.now();
    }
}
