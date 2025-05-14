package com.fernando.ms.posts.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.domain.models.Author;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.posts.app.infrastructure.adapter.output.restclient.mapper.AuthorRestClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements ExternalUserOutputPort {
    private final UserWebClient userWebClient;
    private final AuthorRestClientMapper authorRestClientMapper;
    @Override
    public Flux<Author> findAuthorByUserId(String userId) {
        return authorRestClientMapper.toFluxAuthor(userWebClient.findFollowedByFollowerId(userId));
    }
}
