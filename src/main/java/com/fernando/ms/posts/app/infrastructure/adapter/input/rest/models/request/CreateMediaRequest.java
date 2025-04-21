package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMediaRequest {
    @NotEmpty(message = "Field filenames cannot be null or empty")
    private List<String> filenames;
}
