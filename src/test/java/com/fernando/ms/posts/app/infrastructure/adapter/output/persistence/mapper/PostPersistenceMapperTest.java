package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.posts.app.domain.models.Media;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostMedia;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostPersistenceMapperTest {
    private PostPersistenceMapper postPersistenceMapper;

    @BeforeEach
    void setUp(){
        postPersistenceMapper= Mappers.getMapper(PostPersistenceMapper.class);
    }

    @Test
    @DisplayName("When Mapping FluxPostDocument Expect FluxPost")
    void When_MappingFluxPostDocument_Expect_FluxPost(){
        PostDocument postDataDocument= TestUtilPost.buildPostDocumentMock();
        Flux<Post> postFlux=postPersistenceMapper.toPosts(Flux.just(postDataDocument));

        StepVerifier.create(postFlux)
                .consumeNextWith(post->{
                    assertEquals(post.getContent(),TestUtilPost.buildPostMock().getContent());
                    assertEquals(post.getId(),TestUtilPost.buildPostMock().getId());
                    assertEquals(post.getUserId(),TestUtilPost.buildPostMock().getUserId());
                    assertEquals(post.getMedia(),TestUtilPost.buildPostMock().getMedia());
                }).verifyComplete();
    }

    @Test
    @DisplayName("When Mapping MonoPostDocument Expect MonoPost")
    void When_MappingMonoPostDocument_Expect_MonoPost(){
        PostDocument postDataDocument= TestUtilPost.buildPostDocumentMock();
        Mono<Post> postMono=postPersistenceMapper.toPost(Mono.just(postDataDocument));

        StepVerifier.create(postMono)
                .consumeNextWith(post->{
                    assertEquals(post.getContent(),TestUtilPost.buildPostMock().getContent());
                    assertEquals(post.getId(),TestUtilPost.buildPostMock().getId());
                    assertEquals(post.getUserId(),TestUtilPost.buildPostMock().getUserId());
                    assertEquals(post.getMedia(),TestUtilPost.buildPostMock().getMedia());
                }).verifyComplete();
    }

    @Test
    @DisplayName("When Mapping PostDocument Expect Post")
    void When_MappingPostDocument_Expect_Post(){
        PostDocument postDataDocument= TestUtilPost.buildPostDocumentMock();
        Post post=postPersistenceMapper.toPost(postDataDocument);
        assertEquals(post.getContent(),TestUtilPost.buildPostMock().getContent());
        assertEquals(post.getId(),TestUtilPost.buildPostMock().getId());
        assertEquals(post.getUserId(),TestUtilPost.buildPostMock().getUserId());
        assertEquals(post.getMedia(),TestUtilPost.buildPostMock().getMedia());
    }

    @Test
    @DisplayName("When Mapping Post Expect PostDocument")
    void When_MappingPost_Expect_PostDocument(){
        Post post= TestUtilPost.buildPostMock();
        PostDocument postDocument=postPersistenceMapper.toPostDocument(post);
        assertEquals(postDocument.getContent(),TestUtilPost.buildPostDocumentMock().getContent());
        assertEquals(postDocument.getId(),TestUtilPost.buildPostDocumentMock().getId());
        assertEquals(postDocument.getUserId(),TestUtilPost.buildPostDocumentMock().getUserId());
        assertEquals(postDocument.getMedia().iterator().next().getType(),TestUtilPost.buildPostDocumentMock().getMedia().iterator().next().getType());
        assertEquals(postDocument.getMedia().iterator().next().getUrl(),TestUtilPost.buildPostDocumentMock().getMedia().iterator().next().getUrl());
    }


    @Test
    @DisplayName("When Mapping Set PostMedia Expect Set PostMedia")
    void When_MappingSetPostMedia_Expect_SetMedia(){
        Post post=TestUtilPost.buildPostMock();
        Set<Media> setMedia=post.getMedia();
        Set<PostMedia> setPostMedia=postPersistenceMapper.mapMedia(setMedia);
        assertEquals(setPostMedia.iterator().next().getType(),setMedia.iterator().next().getType());
        assertEquals(setPostMedia.iterator().next().getUrl(),setMedia.iterator().next().getUrl());
    }

    @Test
    @DisplayName("When Mapping PostMedia Expect PostMedia")
    void When_MappingPostMedia_Expect_Media(){
        Post post=TestUtilPost.buildPostMock();
        Set<Media> setMedia=post.getMedia();

        Media media=setMedia.iterator().next();
        PostMedia postMedia=postPersistenceMapper.toPostMedia(media);
        assertEquals(postMedia.getType(),media.getType());
        assertEquals(postMedia.getUrl(),media.getUrl());
    }
}
