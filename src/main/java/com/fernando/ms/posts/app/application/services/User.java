package com.fernando.ms.posts.app.application.services;

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
}
