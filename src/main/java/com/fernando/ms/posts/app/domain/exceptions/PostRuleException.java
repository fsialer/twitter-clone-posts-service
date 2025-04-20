package com.fernando.ms.posts.app.domain.exceptions;

public class PostRuleException  extends RuntimeException{
    public PostRuleException(String message) {
        super(message);
    }
}
