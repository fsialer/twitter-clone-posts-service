package com.fernando.ms.posts.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String names;
    private String email;
    private String userId;
}
