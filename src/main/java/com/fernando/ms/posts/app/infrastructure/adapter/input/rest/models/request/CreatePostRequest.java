package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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
    @NotEmpty(message = "Field media cannot be null or empty")
    private Set<MediaRequest> media;

}
