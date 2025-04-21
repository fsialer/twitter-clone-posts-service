package com.fernando.ms.posts.app.application.ports.input;

import com.fernando.ms.posts.app.domain.models.PostMedia;
import reactor.core.publisher.Flux;

import java.util.List;

public interface PostMediaInputPort {
    Flux<PostMedia> generateSasUrl(List<String> filenames);
}
