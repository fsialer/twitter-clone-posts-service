package com.fernanando.ms.posts.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    POST_NOT_FOUND("POST_MS_001", "Post not found."),
    INTERNAL_SERVER_ERROR("POST_MS_000", "Internal server error.");
    private final String code;
    private final String message;
}
