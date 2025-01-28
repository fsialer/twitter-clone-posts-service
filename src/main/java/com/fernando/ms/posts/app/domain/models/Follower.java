package com.fernando.ms.posts.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follower {
     private String id;
     private Long follower;
     private Long followed;
}
