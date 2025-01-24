package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.Follower;
import reactor.core.publisher.Flux;

public interface ExternalFollowerOutputPort {
    Flux<Follower> findFollowedByFollower(Long followerId, Long page, Long size);
}
