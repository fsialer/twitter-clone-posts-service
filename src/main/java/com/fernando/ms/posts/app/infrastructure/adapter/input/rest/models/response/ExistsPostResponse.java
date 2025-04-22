package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExistsPostResponse {
    private Boolean exists;
}
