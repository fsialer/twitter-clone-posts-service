package com.fernando.ms.posts.app.application.ports.output;

import com.fernando.ms.posts.app.domain.models.Post;

public interface PostBusOutputPort {
    void sendNotification(Post post);
}
