package com.aktic.directdropbackend.controller.message;

import com.aktic.directdropbackend.model.entity.Message;
import com.aktic.directdropbackend.model.request.MessageSameNetRequest;
import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import com.aktic.directdropbackend.service.message.MessageService;
import com.aktic.directdropbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(value = "/same-network/send-message", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponse<MessageInfoResponse>> sendMessageSameNetwork(
            @Valid @ModelAttribute MessageSameNetRequest messageSameNetRequest
    ) {
        return messageService.sendMessageSameNetwork(messageSameNetRequest);
    }
}
