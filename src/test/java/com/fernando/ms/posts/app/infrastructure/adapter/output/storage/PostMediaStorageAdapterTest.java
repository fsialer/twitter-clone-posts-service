package com.fernando.ms.posts.app.infrastructure.adapter.output.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.infrastructure.config.AzureStorageProperties;
import com.fernando.ms.posts.app.utils.TestUtilPostMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostMediaStorageAdapterTest {
    @Mock
    private AzureStorageProperties azureStorageProperties;

    @Mock
    private BlobServiceClientBuilder blobServiceClientBuilder;

    @Mock
    private BlobServiceClient blobServiceClient;

    @Mock
    private BlobContainerClient blobContainerClient;

    @Mock
    private BlockBlobClient blockBlobClient;

    @InjectMocks
    private PostMediaStorageAdapter postMediaStorageAdapter;

    @BeforeEach
    void setUp(){
        // Configuración de propiedades de Azure Storage
        when(azureStorageProperties.getAccountName()).thenReturn("test_account");
        when(azureStorageProperties.getAccountKey()).thenReturn("dGVzdF9rZXk=");
        when(azureStorageProperties.getContainerName()).thenReturn("test_container");
    }


    @Test
    @DisplayName("When Generate SasUrl Expect ValidSasUrl")
    void when_GenerateSasUrl_Expect_ValidSasUrl() {
        String filename = "test_image.jpg";
        String blobUrl = "https://test_account.blob.core.windows.net/test_container/test_image.jpg";

        Mono<PostMedia> result = postMediaStorageAdapter.generateSasUrl(filename);

        StepVerifier.create(result)
                .assertNext(postMedia -> {
                    assert postMedia.getUploadUrl().contains("sv=");
                    assert postMedia.getUploadUrl().contains("sig=");
                    assert postMedia.getBlobUrl().equals(blobUrl);
                })
                .verifyComplete();
    }
}
