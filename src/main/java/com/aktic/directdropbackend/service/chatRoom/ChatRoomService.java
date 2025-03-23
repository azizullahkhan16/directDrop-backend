package com.aktic.directdropbackend.service.chatRoom;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.model.response.ChatRoomResponse;
import com.aktic.directdropbackend.model.response.UserInfoResponse;
import com.aktic.directdropbackend.repository.ChatRoomRepository;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.service.ipService.IPService;
import com.aktic.directdropbackend.service.user.UserService;
import com.aktic.directdropbackend.util.ApiResponse;
import com.aktic.directdropbackend.util.SnowflakeIdGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final IPService ipService;
    private final SnowflakeIdGenerator idGenerator;
    private final UserService userService;

    public ResponseEntity<ApiResponse<Map<String, Object>>> findRoomService(HttpServletRequest request, Long userId) {
        try {
            // Extract user IP address
            String userIp = ipService.extractClientIP(request);

            // Find or create a chat room
            ChatRoom chatRoom = chatRoomRepository.findByIp(userIp)
                    .orElseGet(() -> {
                        ChatRoom newChatRoom = ChatRoom.builder()
                                .roomId(idGenerator.nextId())
                                .ip(userIp)
                                .users(new HashSet<>())
                                .build();
                        return chatRoomRepository.save(newChatRoom);
                    });

            // Find or create a user
            User user = (userId != null)
                    ? userRepository.findByUserId(userId)
                    .orElseGet(() -> userService.createAndSaveUser(chatRoom))
                    : userService.createAndSaveUser(chatRoom);

            // Ensure user is added to the chat room
            if (chatRoom.getUsers().add(user)) {
                chatRoomRepository.save(chatRoom);
            }

            // Convert users to DTOs
            Set<UserInfoResponse> userDTOs = chatRoom.getUsers().stream()
                    .map(UserInfoResponse::new)
                    .collect(Collectors.toSet());

            // Construct response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("room", new ChatRoomResponse(chatRoom));
            responseData.put("user", new UserInfoResponse(user));
            responseData.put("activeUsers", userDTOs);

            return ResponseEntity.ok(new ApiResponse<>(true, "Room found successfully", responseData));

        } catch (Exception e) {
            log.error("Unexpected error occurred while finding room", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> activeUsersService(Long roomId) {
        try {
            // Find chat room by roomId
            ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new NoSuchElementException("Room not found"));

            // Fetch users from the found chat room
            Set<UserInfoResponse> userDTOs = chatRoom.getUsers().stream()
                    .map(UserInfoResponse::new)  // Map to UserInfoResponse DTO
                    .collect(Collectors.toSet());

            // Construct response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("room", new ChatRoomResponse(chatRoom));
            responseData.put("activeUsers", userDTOs);

            return ResponseEntity.ok(new ApiResponse<>(true, "Active users fetched successfully", responseData));

        } catch (NoSuchElementException e) {
            // Handle the case when the chat room is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Room not found", null));
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            log.error("Unexpected error occurred while finding active users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
