package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDataDocument;
import com.fernando.ms.posts.app.utils.TestUtilPostData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostDataPersistenceMapperTest {
    private PostDataPersistenceMapper postDataPersistenceMapper;

    @BeforeEach
    void setUp(){
        postDataPersistenceMapper= Mappers.getMapper(PostDataPersistenceMapper.class);
    }

    @Test
    @DisplayName("When Mapping MonoPostDataDocument Expect MonoPostData")
    void When_MappingMonoPostDataDocument_Expect_MonoPostData(){
        PostDataDocument postDataDocument= TestUtilPostData.buildPostDataDocumentMock();
        Mono<PostData> monoPostData=postDataPersistenceMapper.toPostData(Mono.just(postDataDocument));
        StepVerifier.create(monoPostData)
                .consumeNextWith(postData -> {
                    assertEquals(postData.getPostId(),TestUtilPostData.buildPostDataMock().getPostId());
                    assertEquals(postData.getId(),TestUtilPostData.buildPostDataMock().getId());
                    assertEquals(postData.getUserId(),TestUtilPostData.buildPostDataMock().getUserId());
                    assertEquals(postData.getTypeTarget(),TestUtilPostData.buildPostDataMock().getTypeTarget());
                }).verifyComplete();
    }

    @Test
    @DisplayName("When Mapping PostDataDocument Expect PostData")
    void When_MappingPostDataDocument_Expect_PostData(){
        PostDataDocument postDataDocument= TestUtilPostData.buildPostDataDocumentMock();
        PostData postData=postDataPersistenceMapper.toPostData(postDataDocument);
        assertEquals(postData.getPostId(),TestUtilPostData.buildPostDataMock().getPostId());
        assertEquals(postData.getId(),TestUtilPostData.buildPostDataMock().getId());
        assertEquals(postData.getUserId(),TestUtilPostData.buildPostDataMock().getUserId());
        assertEquals(postData.getTypeTarget(),TestUtilPostData.buildPostDataMock().getTypeTarget());
    }
    @Test
    @DisplayName("When Mapping PostData Expect PostDataDocument")
    void When_MappingPostData_Expect_PostDataDocument(){
        PostData postData= TestUtilPostData.buildPostDataMock();
        PostDataDocument postDataDocument=postDataPersistenceMapper.toPostDataDocument(postData);
        assertEquals(postDataDocument.getPostId(),TestUtilPostData.buildPostDataMock().getPostId());
        assertEquals(postDataDocument.getId(),TestUtilPostData.buildPostDataMock().getId());
        assertEquals(postDataDocument.getUserId(),TestUtilPostData.buildPostDataMock().getUserId());
        assertEquals(postDataDocument.getTypeTarget(),TestUtilPostData.buildPostDataMock().getTypeTarget());
    }

}
