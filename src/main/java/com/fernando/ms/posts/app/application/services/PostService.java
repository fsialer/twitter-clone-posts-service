package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.output.ExternalFollowerOutputPort;
import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.application.services.proxy.IProcess;
import com.fernando.ms.posts.app.application.services.proxy.ProcessFactory;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.bus.PostBusAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PostService implements PostInputPort {
    private final PostPersistencePort postPersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;
    private final ExternalFollowerOutputPort externalFollowerOutputPort;
    private final PostBusAdapter postBusAdapter;

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
        IProcess iProcessPost = ProcessFactory.validateSave(externalUserOutputPort);
        return iProcessPost.doProcess(post).flatMap(postPersistencePort::save).doOnSuccess(postBusAdapter::sendNotification);
    }

    @Override
    public Mono<Post> update(String id, Post post) {
        IProcess iProcessPost = ProcessFactory.validateUpdate(externalUserOutputPort,postPersistencePort,id);
        return iProcessPost.doProcess(post).flatMap(postPersistencePort::save);
    }

    @Override
    public Mono<Void> delete(String id) {
        IProcess iProcessPost = ProcessFactory.validateDelete(postPersistencePort,id);
        return iProcessPost.doProcess(null).flatMap(postDelete-> postPersistencePort.delete(id));
    }

    @Override
    public Mono<Boolean> verify(String id) {
        return postPersistencePort.verify(id);
    }

    @Override
    public Flux<Post> findAllPostMe(Long userId, Long size, Long page) {
        User user= User.builder().id(userId).build();
       return postPersistencePort.findAllPostMe(user,size,page)
               .flatMap(post->externalUserOutputPort.findById(userId)
                           .map(user1 -> {
                               post.setUser(user1);
                               return post;
                           }));
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
                                                .onErrorResume(e -> Mono.just(post))
                                )
                );
    }
}
