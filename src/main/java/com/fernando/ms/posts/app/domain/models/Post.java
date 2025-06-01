package com.fernando.ms.posts.app.domain.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    private String id;
    private String content;
    private LocalDateTime datePost;
    private String userId;
    private Set<Media> media;
    private Author author;
}
