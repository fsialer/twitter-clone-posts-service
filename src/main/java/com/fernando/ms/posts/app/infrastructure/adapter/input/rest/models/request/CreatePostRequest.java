package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequest {

    @NotBlank(message = "Field content cannot be null or blank")
    private String content;
    @NotNull(message = "Field userId cannot be null")
    private Long userId;

}
