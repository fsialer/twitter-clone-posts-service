package com.fernando.ms.posts.app.infrastructure.adapter.output.storage;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.fernando.ms.posts.app.application.ports.output.PostMediaStoragePort;
import com.fernando.ms.posts.app.domain.models.PostMedia;
import com.fernando.ms.posts.app.infrastructure.config.AzureStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class PostMediaStorageAdapter implements PostMediaStoragePort {
    private final AzureStorageProperties azureStorageProperties;
    @Override
    public Mono<PostMedia> generateSasUrl(String filename) {
        String endpoint = String.format("https://%s.blob.core.windows.net", azureStorageProperties.getAccountName());
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(new StorageSharedKeyCredential( azureStorageProperties.getAccountName(),  azureStorageProperties.getAccountKey()))
                .buildClient();

        BlobContainerClient containerClient = serviceClient.getBlobContainerClient(azureStorageProperties.getContainerName());
        BlockBlobClient blobClient = containerClient.getBlobClient(filename).getBlockBlobClient();
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusMinutes(5), // Expira en 5 minutos
                new BlobSasPermission().setWritePermission(true).setCreatePermission(true)
        ).setStartTime(OffsetDateTime.now());
        String sasToken = blobClient.generateSas(sasValues);
        return Mono.just(PostMedia.builder()
                .uploadUrl(blobClient.getBlobUrl() + "?" + sasToken)
                .blobUrl(blobClient.getBlobUrl())
                .build());
    }
}
