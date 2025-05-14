package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostAuthorResponse(
         String id,
         String content,
         LocalDateTime datePost,
         String author
) {
}
