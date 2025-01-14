package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequest {
    @NotBlank(message = "Field content cannot be null or blank")
    private String content;
}
