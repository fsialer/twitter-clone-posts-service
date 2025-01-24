package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper;


import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRestClientMapper {
    User toUser (UserResponse userResponse);
}
