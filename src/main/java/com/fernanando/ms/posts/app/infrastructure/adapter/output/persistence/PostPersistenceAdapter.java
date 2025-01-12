package com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernanando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernanando.ms.posts.app.domain.models.Post;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostPersistenceMapper;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;
import com.fernanando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostPersistencePort {
    private final PostReactiveMongoRepository postReactiveMongoRepository;
    private final PostPersistenceMapper postPersistenceMapper;
    @Override
    public Flux<Post> findAll() {
        return postPersistenceMapper.toPosts(postReactiveMongoRepository.findAll());
    }

    @Override
    public Mono<Post> findById(String id) {
        return postReactiveMongoRepository.findById(id).map(postPersistenceMapper::toPost);
    }

    @Override
    public Mono<Post> save(Post post) {
        PostUser postUser = PostUser.builder()
                .userId(post.getUser().getId())
                .build();
        PostDocument postDocument=postPersistenceMapper.toPostDocument(post);
        postDocument.setPostUser(postUser);
        return postPersistenceMapper.toPost(postReactiveMongoRepository.save(postDocument));
    }
}
