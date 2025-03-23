package com.aktic.directdropbackend.service.user;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.model.request.UpdateUserRequest;
import com.aktic.directdropbackend.model.response.UserInfoResponse;
import com.aktic.directdropbackend.repository.ChatRoomRepository;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.util.ApiResponse;
import com.aktic.directdropbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final ChatRoomRepository chatRoomRepository;
    private final MongoTemplate mongoTemplate;

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


    public ResponseEntity<ApiResponse<UserInfoResponse[]>> findUser(Long userId, String username, String ipAddress) {
        try {
            Set<User> users;

            if (ipAddress != null) {
                // Find chat room by IP address
                ChatRoom chatRoom = chatRoomRepository.findByIp(ipAddress)
                        .orElseThrow(() -> new NoSuchElementException("Room not found"));
                users = chatRoom.getUsers();
            }else {
                User user = userRepository.findByUserId(userId)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));

                users = user.getChatRoom().getUsers();
            }

            // If no username is provided, return all users (filtered if ipAddress was provided)
            if (username == null && ipAddress == null) {
                users.removeIf(user -> userId != null && user.getUserId().equals(userId)); // Exclude self
                UserInfoResponse[] userDTOs = users.stream()
                        .map(UserInfoResponse::new)
                        .toArray(UserInfoResponse[]::new);
                return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", userDTOs));
            }

            // Perform full-text search on username
            Query query = new Query();
            query.addCriteria(Criteria.where("username").regex(username, "i"));
            List<User> usernameMatches = mongoTemplate.find(query, User.class);

            // Apply filtering only if ipAddress was provided
            Set<User> filteredUsers = (ipAddress == null)
                    ? new HashSet<>(usernameMatches) // No filtering, return all matches
                    : usernameMatches.stream().filter(users::contains).collect(Collectors.toSet()); // Filter by chat room

            if (filteredUsers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No users found", null));
            }

            filteredUsers.removeIf(user -> userId != null && user.getUserId().equals(userId)); // Exclude self

            UserInfoResponse[] userDTOs = filteredUsers.stream()
                    .map(UserInfoResponse::new)
                    .toArray(UserInfoResponse[]::new);

            return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", userDTOs));

        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Room not found", null));

        } catch (Exception e) {
            log.error("Unexpected error occurred while finding user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
