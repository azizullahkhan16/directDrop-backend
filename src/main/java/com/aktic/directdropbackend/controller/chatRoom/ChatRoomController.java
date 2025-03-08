package com.aktic.directdropbackend.controller.chatRoom;

import com.aktic.directdropbackend.service.chatRoom.ChatRoomService;
import com.aktic.directdropbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/find-room")
    public ResponseEntity<ApiResponse<?>> findRoomController(@RequestParam Long roomId, @RequestParam Long userId) {
        return chatRoomService.findRoomService(roomId, userId);
    }
}

