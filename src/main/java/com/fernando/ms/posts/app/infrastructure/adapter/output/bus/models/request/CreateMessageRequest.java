package com.fernando.ms.posts.app.infrastructure.adapter.output.bus.models.request;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMessageRequest {
    private Long userId;
    private String targetType;
    private String targetId;
    private String content;
    private String datePost;
}
