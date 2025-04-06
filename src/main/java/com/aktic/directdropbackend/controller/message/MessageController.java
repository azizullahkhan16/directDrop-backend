package com.aktic.directdropbackend.controller.message;

import com.aktic.directdropbackend.model.entity.Message;
import com.aktic.directdropbackend.model.request.MessageAcrossNetRequest;
import com.aktic.directdropbackend.model.request.MessageSameNetRequest;
import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import com.aktic.directdropbackend.service.message.MessageService;
import com.aktic.directdropbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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


    @PostMapping(value = "/across-network/send-message", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponse<MessageInfoResponse>> sendMessageAcrossNetwork(
            @Valid @ModelAttribute MessageAcrossNetRequest messageAcrossNetRequest
            ) {
        return messageService.sendMessageAcrossNetwork(messageAcrossNetRequest);
    }

    @GetMapping("/same-network/get-messages/{userId}")
    public ResponseEntity<ApiResponse<Page<MessageInfoResponse>>> getMessagesSameNetwork(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String username
    ) {
        return messageService.getMessagesSameNetwork(userId, pageNumber, limit, keyword, username);
    }

    @GetMapping("/across-network/get-messages/{userId}")
    public ResponseEntity<ApiResponse<Page<MessageInfoResponse>>> getMessagesAcrossNetwork(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String username
    ) {
        return messageService.getMessagesAcrossNetwork(userId, pageNumber, limit, keyword, username);
    }


}
