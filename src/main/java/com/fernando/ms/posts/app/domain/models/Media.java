package com.fernando.ms.posts.app.domain.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Media {
    private String type;
    private String url;
}
