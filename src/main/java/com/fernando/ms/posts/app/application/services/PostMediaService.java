package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.input.PostMediaInputPort;
import com.fernando.ms.posts.app.application.ports.output.PostMediaStoragePort;
import com.fernando.ms.posts.app.domain.models.PostMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMediaService implements PostMediaInputPort {

    private final PostMediaStoragePort postMediaStoragePort;

    @Override
    public Flux<PostMedia> generateSasUrl(List<String> filenames) {
        return Flux.fromIterable(filenames).flatMap(postMediaStoragePort::generateSasUrl);
    }
}
