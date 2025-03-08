package com.aktic.directdropbackend.service.chatRoom;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.repository.ChatRoomRepository;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<?>> findRoomService(Long roomId, Long userId) {
        try {
            if (roomId != null && userId != null) {
                Optional<ChatRoom> chatRoom = chatRoomRepository.findByRoomId(roomId);

                if(!chatRoom.isEmpty()) {
                    ChatRoom room = chatRoom.get();
                    if(room.getUsers().contains(userId)) {

                    } else {
                        Optional<User> user = userRepository.findByUserId(userId);
                        if(!user.isEmpty()) {
                            if(room.getUsers().contains(user.get().getUserId())) {

                            } else {
                                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                        .body(new ApiResponse<>(false, "User can not access this room", null));
                            }
                        }
                    }
                }

            }

            return null;

        }catch (Exception e) {
            log.error("Unexpected error occurred while finding room", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
