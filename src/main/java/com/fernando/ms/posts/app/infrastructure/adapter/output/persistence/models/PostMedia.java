package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMedia {
    private String type;
    private String url;
}
