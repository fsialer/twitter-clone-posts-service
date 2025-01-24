package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostPersistenceMapper {

    default Flux<Post> toPosts(Flux<PostDocument> posts) {
        return posts.map(this::toPost); // Convierte cada elemento individualmente
    }

    default Mono<Post> toPost(Mono<PostDocument> post) {
        return post.map(this::toPost); // Convierte cada elemento individualmente
    }

    @Mapping(target = "user",expression = "java(toUser(post))")
    Post toPost(PostDocument post);

    @Mapping(target = "datePost",expression = "java(mapDatePost())")
    @Mapping(target = "createdAt",expression = "java(mapCreatedAt())")
    @Mapping(target = "updatedAt",expression = "java(mapUpdatedAt())")
    PostDocument toPostDocument(Post post);

    default LocalDateTime mapDatePost(){
        return LocalDateTime.now();
    }

    default LocalDateTime mapCreatedAt(){
        return LocalDateTime.now();
    }

    default LocalDateTime mapUpdatedAt(){
        return LocalDateTime.now();
    }


    default User toUser(PostDocument posts){
        return User.builder()
                .id(posts.getPostUser().getUserId())
                .build();
    }


    default PostUser toPostUser(User user){
        return PostUser.builder()
                .userId(user.getId())
                .build();
    }

    default Iterable<PostUser> toPostUsers(Iterable<User> users){
        List<PostUser> postUsers = new ArrayList<>();
        users.forEach(user -> postUsers.add(toPostUser(user)));
        return postUsers;
    }

}
