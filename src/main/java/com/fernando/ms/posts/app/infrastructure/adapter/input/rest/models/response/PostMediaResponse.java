package com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record PostMediaResponse (
        String uploadUrl,
        String blobUrl
){}
