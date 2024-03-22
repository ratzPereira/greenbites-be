package com.ratz.greenbites.services.external;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureStorageService {

    private final String containerName = "greenbitsimage";

    @Value("${AZURE_STORAGE_CONNECTION_STRING}")
    private String connectionString;

    public String uploadFile(MultipartFile file) {
        try {

            String uniqueFileIdentifier = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = uniqueFileIdentifier + extension;
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            BlobClientBuilder blobClientBuilder = new BlobClientBuilder();
            blobClientBuilder.connectionString(connectionString);
            blobClientBuilder.containerName(containerName);
            blobClientBuilder.blobName(filename);
            BlobClient blobClient = blobClientBuilder.buildClient();
            blobClient.upload(file.getInputStream(), file.getSize(), true);


            return blobClient.getBlobUrl();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload", e);
        }
    }
}
