package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;

import com.fernando.ms.posts.app.domain.enums.TypeTarget;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.validation.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostDataRequest {
    @NotBlank(message = "Field postId cannot be null or blank")
    private String postId;
    @NotNull(message = "Field typeTarget cannot be null or blank")
    @EnumValidator(enumClass = TypeTarget.class, message = "TypeTarget not valid")
    private String typeTarget;
}
