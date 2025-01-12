package com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "posts")
public class PostDocument {
    private String id;
    private String content;
    private LocalDateTime datePost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
