package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "posts")
public class PostDocument {
    @Id
    private String id;
    private String content;
    @Indexed
    private LocalDateTime datePost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Indexed
    private String userId;
    private Set<PostMedia> media;

}

