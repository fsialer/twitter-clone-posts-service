package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String id;
    private String content;
    private LocalDateTime datePost;
}
