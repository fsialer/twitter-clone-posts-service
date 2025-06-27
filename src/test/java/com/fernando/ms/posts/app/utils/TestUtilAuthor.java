package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.domain.models.Author;

public class TestUtilAuthor {
    public static Author buildAuthorMock(){
        return Author.builder()
                .id("5d4d75sd4sd6sd")
                .fullName("Fernando Sialer")
                .userId("dsd5sd5s4d125s4d5sds")
                .build();
    }
}
