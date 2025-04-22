package com.fernando.ms.posts.app.domain.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostMedia {
    private String uploadUrl;
    private String blobUrl;
}
