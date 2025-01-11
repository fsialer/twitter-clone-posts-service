package com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostPersistenceMapper;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostReactiveMongoRepository;
import com.fernanando.ms.posts.app.utils.TestUtilPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostPersistenceAdapterTest {

    @Mock
    private PostReactiveMongoRepository postReactiveMongoRepository;

    @Mock
    private PostPersistenceMapper postPersistenceMapper;

    @InjectMocks
    private PostPersistenceAdapter postPersistenceAdapter;

    @Test
    @DisplayName("When Posts Are Correct Expect A List Posts Correct")
    void When_UsersAreCorrect_Expect_AListUsersCorrect() {
        Post post= TestUtilPost.buildPostMock();
        PostDocument postDocument= TestUtilPost.buildPostDocumentMock();
        when(postReactiveMongoRepository.findAll()).thenReturn(Flux.just(postDocument));
        when(postPersistenceMapper.toPosts(any(Flux.class))).thenReturn(Flux.just(post));
        Flux<Post> result = postPersistenceAdapter.findAll();
        StepVerifier.create(result)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postReactiveMongoRepository,times(1)).findAll();
        Mockito.verify(postPersistenceMapper,times(1)).toPosts(any(Flux.class));
    }

}
