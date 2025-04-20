package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "posts_data")
@EqualsAndHashCode
public class PostDataDocument {
    @Id
    private String id;
    @Indexed
    private String postId;
    private String typeTarget;
    @Indexed
    private String userId;
    private LocalDateTime createdAt;
}
