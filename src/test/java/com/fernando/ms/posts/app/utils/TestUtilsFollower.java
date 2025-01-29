package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.FollowerResponse;

public class TestUtilsFollower {
    public static FollowerResponse buildFollowerResponseMock(){
        return FollowerResponse.builder()
                .followed(1L)
                .follower(2L)
                .id("854854154s4f5sd4")
                .build();
    }

    public static Follower buildFollowerMock(){
        return Follower.builder()
                .follower(1L)
                .followed(2L)
                .id("854854154s4f5sd4")
                .build();
    }
}
