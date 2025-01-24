package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper;

import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.FollowerResponse;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface FollowerRestClientMapper {
    default Flux<Follower> toFollower(FollowerResponse followerResponse){
        return Flux.just(Follower.builder()
                .id(followerResponse.getId())
                .follower(followerResponse.getFollower())
                .followed(followerResponse.getFollowed())
                .build());
    }
}
