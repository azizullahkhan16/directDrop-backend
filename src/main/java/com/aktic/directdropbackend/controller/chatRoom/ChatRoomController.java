package com.aktic.directdropbackend.controller.chatRoom;

import com.aktic.directdropbackend.service.chatRoom.ChatRoomService;
import com.aktic.directdropbackend.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/find-room")
    public ResponseEntity<ApiResponse<Map<String, Object>>> findRoomController(HttpServletRequest request,
                                                               @RequestParam(value = "userId", required = false) Long userId) {
        return chatRoomService.findRoomService(request, userId);
    }

    @GetMapping("/active-users/{roomId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> activeUsersController(@PathVariable Long roomId) {
        return chatRoomService.activeUsersService(roomId);
    }
}

