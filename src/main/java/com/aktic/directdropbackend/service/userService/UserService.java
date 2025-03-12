package com.aktic.directdropbackend.service.userService;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.model.request.UpdateUserRequest;
import com.aktic.directdropbackend.model.response.UserInfoResponse;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.util.ApiResponse;
import com.aktic.directdropbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SnowflakeIdGenerator idGenerator;

    public User createAndSaveUser(ChatRoom chatRoom) {
        Long userId = idGenerator.nextId();
        User newUser = User.builder()
                .userId(userId)
                .username("user_" + userId)
                .chatRoom(chatRoom)
                .build();
        return userRepository.save(newUser);
    }

    public ResponseEntity<ApiResponse<UserInfoResponse>> updateUserInfoService(Long userId, UpdateUserRequest request) {
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

            boolean updated = false;

            if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
                user.setUsername(request.getUsername());
                updated = true;
            }

            if (request.getIsActive() != null && !request.getIsActive().equals(user.getIsActive())) {
                user.setIsActive(request.getIsActive());
                updated = true;
            }

            if (updated) {
                userRepository.save(user);
                log.info("User info updated successfully for userId: {}", userId);
                return ResponseEntity.ok(new ApiResponse<>(true, "User info updated", new UserInfoResponse(user)));
            } else {
                log.info("No changes detected for userId: {}", userId);
                return ResponseEntity.ok(new ApiResponse<>(true, "No changes detected", new UserInfoResponse(user)));
            }

        } catch (NoSuchElementException e) {
            log.warn("User not found: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "User not found", null));
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user info for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }


}
