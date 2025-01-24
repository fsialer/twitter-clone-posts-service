package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.User;

public class TestUtilsUser {

    public static User buildUserMock(){
        return User.builder()
                .id(1L)
                .username("user")
                .names("User Test")
                .email("example@mail.com")
                .build();
    }
}
