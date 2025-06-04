package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.posts.app.application.ports.output.PostDataPersistencePort;
import com.fernando.ms.posts.app.domain.models.PostData;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.mapper.PostDataPersistenceMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository.PostDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostDataPersistenceAdapter implements PostDataPersistencePort {
    private final PostDataRepository postDataRepository;
    private final PostDataPersistenceMapper postDataPersistenceMapper;

    @Override
    public Mono<Void> save(PostData postData) {
        return postDataPersistenceMapper.toPostData(postDataRepository.save(postDataPersistenceMapper.toPostDataDocument(postData))).then();
    }

    @Override
    public Mono<PostData> findById(String id) {
        return postDataPersistenceMapper.toPostData(postDataRepository.findById(id));
    }

    @Override
    public Mono<Boolean> verifyPostData(String postId,String userId) {
        return postDataRepository.existsByPostIdAndUserId(postId,userId);
    }

    @Override
    public Mono<Void> delete(String id) {
        return postDataRepository.deleteById(id).then();
    }

    @Override
    public Mono<Long> countPostDataByPost(String postId) {
        return postDataRepository.countPostDataByPostId(postId);
    }

    @Override
    public Mono<PostData> findByPostIdAndUserId(String postId, String userId) {
        return postDataPersistenceMapper.toPostData(postDataRepository.findByPostIdAndUserId(postId,userId));
    }
}
