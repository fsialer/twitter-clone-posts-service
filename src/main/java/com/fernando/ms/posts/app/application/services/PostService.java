package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.output.ExternalFollowerOutputPort;
import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.posts.app.domain.models.Follower;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService implements PostInputPort {
    private final PostPersistencePort postPersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;
    private final ExternalFollowerOutputPort externalFollowerOutputPort;

    @Override
    public Flux<Post> findAll() {
        return postPersistencePort.findAll();
    }

    @Override
    public Mono<Post> findById(String id) {
        return postPersistencePort.findById(id).switchIfEmpty(Mono.error(PostNotFoundException::new));
    }

    @Override
    public Mono<Post> save(Post post) {
        return externalUserOutputPort.verify(post.getUser().getId())
                .flatMap(exist->{
                    if(Boolean.FALSE.equals(exist)){
                        return Mono.error(new UserNotFoundException());
                    }
                    return postPersistencePort.save(post);
                });
    }

    @Override
    public Mono<Post> update(String id, Post post) {
        return externalUserOutputPort.verify(post.getUser().getId())
                .flatMap(exist->{
                    if(Boolean.FALSE.equals(exist)){
                        return Mono.error(new UserNotFoundException());
                    }
                    return postPersistencePort.findById(id)
                            .switchIfEmpty(Mono.error(PostNotFoundException::new))
                            .flatMap(postUpdated->{
                                postUpdated.setContent(post.getContent());
                                return postPersistencePort.save(postUpdated);
                            });
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return postPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostNotFoundException::new))
                .flatMap(postDelete->{
                    return postPersistencePort.delete(id);
                });
    }

    @Override
    public Mono<Boolean> verify(String id) {
        return postPersistencePort.verify(id);
    }

    @Override
    public Flux<Post> findAllPostMe(Long userId, Long size, Long page) {
        User user= User.builder().id(userId).build();
       return postPersistencePort.findAllPostMe(user,size,page)
               .flatMap(post->{
                   return externalUserOutputPort.findById(userId)
                           .map(user1 -> {
                               post.setUser(user1);
                               return post;
                           });
               });

    }

    @Override
    public Flux<Post> findAllPostRecent(Long followerId, Long size, Long page) {
        return externalFollowerOutputPort.findFollowedByFollower(followerId)
                .map(followed -> User.builder().id(followed.getFollowed()).build())
                .collectList()
                .flatMapMany(followedList ->
                        postPersistencePort.findAllPostRecent(followedList,size,page)
                                .flatMap(post ->
                                        externalUserOutputPort.findById(post.getUser().getId())
                                                .map(user -> {
                                                    post.setUser(user);
                                                    return post;
                                                })
                                                .onErrorResume(e -> {
                                                    return Mono.just(post);
                                                })
                                )
                );
    }
}
