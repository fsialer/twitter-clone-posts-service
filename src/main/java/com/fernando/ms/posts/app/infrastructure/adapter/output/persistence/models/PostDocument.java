package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private LocalDateTime datePost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostUser postUser;

}

