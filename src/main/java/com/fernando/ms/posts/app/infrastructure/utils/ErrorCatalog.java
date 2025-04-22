package com.fernando.ms.posts.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    POST_NOT_FOUND("POST_MS_001", "Post not found."),
    POST_BAD_PARAMETERS("POST_MS_002", "Invalid parameters for creation post"),
    WEB_CLIENT_FAILED("POST_MS_003","CLIENT FAILED"),
    USER_NOT_FOUND("POST_MS_004","User not found"),
    POST_RULE_EXCEPTION("POST_MS_005","Rule invalid"),
    POST_DATA_NOT_FOUND("POST_MS_006","PostData not found."),
    POST_INTERNAL_SERVER_ERROR("POST_MS_000", "Internal server error.");
    private final String code;
    private final String message;
}
