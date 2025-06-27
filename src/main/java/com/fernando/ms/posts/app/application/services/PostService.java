package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.exceptions.AuthorNotFoundException;
import com.fernando.ms.posts.app.domain.exceptions.PostNotFoundException;
import com.fernando.ms.posts.app.domain.models.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService implements PostInputPort {
    private final PostPersistencePort postPersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;

    @Override
    public Flux<Post> findAll() {
        return postPersistencePort.findAll();
    }

    @Override
    public Mono<Post> findById(String id) {
        return postPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostNotFoundException::new))
                .flatMap(post->
                    externalUserOutputPort.findByUserId(post.getUserId())
                            .map(author-> {
                                post.setAuthor(author);
                                return post;
                            })
                );
    }

    @Override
    public Mono<Post> save(Post post) {
        return postPersistencePort.save(post);
    }

    @Override
    public Mono<Post> update(String id, Post post) {
        return postPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostNotFoundException::new))
                .flatMap(postUpdated->{
                    postUpdated.setContent(post.getContent());
                    return postPersistencePort.save(postUpdated);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return postPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(PostNotFoundException::new))
                .flatMap(post->postPersistencePort.delete(post.getId()));
    }

    @Override
    public Mono<Boolean> verify(String id) {
        return postPersistencePort.verify(id);
    }

    @Override
    public Flux<Post> recent(String userId,int page, int size) {
        return externalUserOutputPort.findAuthorByUserId(userId)
                .collectList()
                .flatMapMany(authors-> postPersistencePort.recent(authors,page,size)
                        .flatMap(post ->{
                            authors.stream()
                                    .filter(author -> author.getUserId().equals(post.getUserId()))
                                    .findFirst()
                                    .ifPresent(post::setAuthor);
                            return Flux.just(post);
                            }
                        ).log()
                );
    }

    @Override
    public Flux<Post> me(String userId, int page, int size) {
        return externalUserOutputPort.findByUserId(userId)
                .switchIfEmpty(Mono.error(AuthorNotFoundException::new))
                .flatMapMany(author->
                        postPersistencePort.me(userId,page,size)
                            .doOnNext(post -> post.setAuthor(author)
                            )
                );
    }

    @Override
    public Mono<Long> countPostByUser(String userId) {
        return postPersistencePort.countPostByUser(userId);
    }
}
