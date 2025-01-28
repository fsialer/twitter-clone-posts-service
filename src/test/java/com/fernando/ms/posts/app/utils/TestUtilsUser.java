package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.UserResponse;

public class TestUtilsUser {

    public static User buildUserMock(){
        return User.builder()
                .id(1L)
                .username("user")
                .names("User Test")
                .email("example@mail.com")
                .build();
    }

    public static ExistsUserResponse buildExistsUserResponseMock(){
        return ExistsUserResponse.builder()
                .exists(true)
                .build();
    }

    public static UserResponse buildUserResponseMock(){
        return UserResponse.builder()
                .id(1L)
                .email("example@mail.com")
                .username("user10")
                .names("user")
                .build();
    }
}
