package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.FollowerWebClient;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.FollowerRestClientMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.FollowerResponse;
import com.fernando.ms.posts.app.utils.TestUtilsFollower;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FollowerRestClientAdapterTest {
    @Mock
    private FollowerWebClient followerWebClient;

    @Mock
    private FollowerRestClientMapper followerRestClientMapper;

    @InjectMocks
    private FollowerRestClientAdapter followerRestClientAdapter;

    @Test
    @DisplayName("When Follower Identifier Is Correct Expect A List Followed Correct")
    void When_FollowerIdentifierIsCorrect_Expect_AListFollowedCorrect(){
        FollowerResponse followerResponse=TestUtilsFollower.buildFollowerResponseMock();
        Follower follower=TestUtilsFollower.buildFollowerMock();
        when(followerWebClient.findFollowed(anyLong())).thenReturn(Flux.just(followerResponse));
        when(followerRestClientMapper.toFollower(any(FollowerResponse.class))).thenReturn(Flux.just(follower));

        Flux<Follower> followerFlux=followerRestClientAdapter.findFollowedByFollower(1L);
        StepVerifier.create(followerFlux)
                .expectNext(follower)
                .verifyComplete();
        Mockito.verify(followerWebClient,times(1)).findFollowed(anyLong());
        Mockito.verify(followerRestClientMapper,times(1)).toFollower(any(FollowerResponse.class));
    }
}
