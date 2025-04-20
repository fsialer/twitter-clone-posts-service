package com.fernando.ms.posts.app.domain.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostData {
    private String id;
    private String postId;
    private String typeTarget;
    private String userId;
}
