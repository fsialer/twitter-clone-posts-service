package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.model.response;

import lombok.Builder;

@Builder
public record UserResponse(String id,String names,String lastNames) {
}
