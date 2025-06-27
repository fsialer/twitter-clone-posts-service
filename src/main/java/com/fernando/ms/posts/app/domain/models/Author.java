package com.fernando.ms.posts.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
    private String id;
    private String fullName;
    private String userId;
}
