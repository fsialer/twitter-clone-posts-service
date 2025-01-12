package com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.models.response;

import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.models.response.enums.ErrorType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private ErrorType type; //Functional, System
    private String message;
    private List<String> details;
    private String timestamp;
}