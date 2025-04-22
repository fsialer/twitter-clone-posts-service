package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.PostMedia;
import reactor.core.publisher.Mono;

public interface PostMediaStoragePort {
    Mono<PostMedia> generateSasUrl(String filename);
}
