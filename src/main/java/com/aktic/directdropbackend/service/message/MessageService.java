package com.aktic.directdropbackend.service.message;

import com.aktic.directdropbackend.service.fileStorage.FileStorageService;
import com.aktic.directdropbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final FileStorageService fileStorageService;


    public ResponseEntity<ApiResponse<String>> sendMessage(MultipartFile file) {
        try{
            String path = fileStorageService.save(file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Message saved successfully", path));
        }catch (Exception e) {
            log.error("Unexpected error occurred while finding user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
