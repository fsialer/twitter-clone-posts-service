package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request;

import com.fernando.ms.posts.app.domain.enums.TypeMedia;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.validation.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaRequest {
    @EnumValidator(enumClass = TypeMedia.class, message = "TypeMedia not valid")
    private String type;

    @NotBlank(message = "Field url cannot be null or blank")
    private String url;
}
