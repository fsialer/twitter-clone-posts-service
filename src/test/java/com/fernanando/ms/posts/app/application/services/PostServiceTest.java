package com.fernanando.ms.posts.app.application.services;

import com.fernanando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernanando.ms.posts.app.domain.models.Post;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostPersistencePort postPersistencePort;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("When Posts Information Is Correct Expect A List Posts")
    void When_UsersInformationIsCorrect_Expect_AListUsers(){
        Post post= TestUtilPost.buildPostMock();
        when(postPersistencePort.findAll()).thenReturn(Flux.just(post));

        Flux<Post> posts=postService.findAll();
        StepVerifier.create(posts)
                .expectNext(post)
                .verifyComplete();
        Mockito.verify(postPersistencePort,times(1)).findAll();
    }

}
