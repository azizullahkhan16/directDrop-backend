package com.aktic.directdropbackend.service.fileStorage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CloudinaryFileStorageService implements FileStorageService {

    private final Cloudinary cloudinary;

    @Value("${application.storage.folder-name}")
    private String folderName; // Changed long to String

    @Override
    public void init() {
        // Cloudinary does not require initialization
    }

    @Override
    public String save(MultipartFile file) {
        try {
            // Restrict file size (e.g., 5MB = 5 * 1024 * 1024 bytes)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("File size exceeds the 5MB limit");
            }

            // Restrict file type (allow only images)
            String contentType = file.getContentType();
            if (contentType != null && (contentType.startsWith("video/") || contentType.startsWith("audio/"))) {
                throw new IllegalArgumentException("Videos and audio files are not allowed.");
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", folderName // Store in the specified folder
                    ));
            return uploadResult.get("secure_url").toString(); // Return the file URL
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            return new UrlResource(filename);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            String publicId = extractPublicId(filename);
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Bulk delete is not supported for Cloudinary.");
    }

    @Override
    public Stream<Path> loadAll() {
        throw new UnsupportedOperationException("Loading all files is not supported for Cloudinary.");
    }

    private String extractPublicId(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return folderName + "/" + filename; // Include folder name in publicId
    }
}
