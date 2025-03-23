package com.aktic.directdropbackend.controller.message;

import com.aktic.directdropbackend.service.message.MessageService;
import com.aktic.directdropbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send-message")
    public ResponseEntity<ApiResponse<String>> sendMessage(@RequestParam MultipartFile file) {
        return messageService.sendMessage(file);
    }
}
