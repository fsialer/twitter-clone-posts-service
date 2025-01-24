package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.domain.models.User;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostPersistenceMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostReactiveMongoRepository;
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

        PostDocument postDocument=postPersistenceMapper.toPostDocument(post);
        if(post.getId()==null){
            PostUser postUser = PostUser.builder()
                    .userId(post.getUser().getId())
                    .build();
            postDocument.setPostUser(postUser);
        }

        return postPersistenceMapper.toPost(postReactiveMongoRepository.save(postDocument));
    }

    @Override
    public Mono<Void> delete(String id) {
        return postReactiveMongoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> verify(String id) {
        return postReactiveMongoRepository.existsById(id);
    }

    @Override
    public Flux<Post> findAllPostMe(User user) {
        return postPersistenceMapper.toPosts(postReactiveMongoRepository.findAllByPostUserOrderByDatePostDesc(postPersistenceMapper.toPostUser(user)));
    }

    @Override
    public Flux<Post> findAllPostMe(User user, Long size, Long page) {
        return postPersistenceMapper.toPosts(postReactiveMongoRepository.findAllUserAndPageAndSize(postPersistenceMapper.toPostUser(user),size,page));
    }

    @Override
    public Flux<Post> findAllPostRecent(Iterable<User> followed,Long size,Long page) {

        return postPersistenceMapper.toPosts(postReactiveMongoRepository.findAllPostRecent(postPersistenceMapper.toPostUsers(followed),size,page));
    }
}
