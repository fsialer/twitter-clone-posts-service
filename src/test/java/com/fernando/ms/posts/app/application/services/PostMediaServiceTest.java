package com.fernando.ms.posts.app.application.services;

import com.fernando.ms.posts.app.application.ports.output.PostMediaStoragePort;
import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.utils.TestUtilPostMedia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostMediaServiceTest {
    @Mock
    private PostMediaStoragePort postMediaStoragePort;

    @InjectMocks
    private PostMediaService postMediaService;

    @Test
    @DisplayName("when Generate SasUrl Expect Valid SasUrl")
    void when_GenerateSasUrl_Expect_ValidSasUrl() {
        String filename = "test_image.jpg";
        PostMedia mockPostMedia = TestUtilPostMedia.buildPostMedia();

        when(postMediaStoragePort.generateSasUrl(anyString())).thenReturn(Mono.just(mockPostMedia));
        Flux<PostMedia> result = postMediaService.generateSasUrl(List.of(filename));

        StepVerifier.create(result)
                .assertNext(postMedia -> {
                    assert postMedia.getUploadUrl().equals("https://fernando-ms-posts.blob.core.windows.net/media/2023-10-01/test_image.jpg?sv=2023-10-01T00%3A00%3A00Z&se=2023-10-01T00%3A00%3A00Z&sr=b&sp=racwdl&sig=signature");
                    assert postMedia.getBlobUrl().equals("https://fernando-ms-posts.blob.core.windows.net/media/2023-10-01/test_image.jpg");
                })
                .verifyComplete();

        verify(postMediaStoragePort, times(1)).generateSasUrl(filename);
    }
}
