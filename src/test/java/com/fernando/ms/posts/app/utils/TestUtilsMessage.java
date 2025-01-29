package com.fernando.ms.posts.app.utils;

import com.fernando.ms.posts.app.infrastructure.adapter.output.bus.models.request.CreateMessageRequest;

import java.time.LocalDateTime;

public class TestUtilsMessage {

    public static CreateMessageRequest buildCreateMessageRequestMock(){
        return CreateMessageRequest.builder()
                .userId(1L)
                .content("Hello everybody")
                .datePost(LocalDateTime.now().toString())
                .targetId("5478d5aq548qww966")
                .targetType("POST")
                .build();
    }
}
