package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response;

import lombok.Builder;

@Builder
public record ExistsPostDataResponse(Boolean exists) {
}
