package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerResponse {
    private String id;
    private Long follower;
    private Long followed;
}
