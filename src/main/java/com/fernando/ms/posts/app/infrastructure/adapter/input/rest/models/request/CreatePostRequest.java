package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequest {

    @NotBlank(message = "Field content cannot be null or blank")
    private String content;
    @Valid
    private Set<MediaRequest> media;
    @NotNull(message = "Field datePost cannot be null")
    private LocalDateTime datePost;

}
