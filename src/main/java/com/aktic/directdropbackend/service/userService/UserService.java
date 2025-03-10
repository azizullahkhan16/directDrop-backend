package com.aktic.directdropbackend.service.userService;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
                .createdAt(Instant.now())
                .build();
        return userRepository.save(newUser);
    }
}
