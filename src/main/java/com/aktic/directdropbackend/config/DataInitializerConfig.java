package com.aktic.directdropbackend.config;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.repository.ChatRoomRepository;
import com.aktic.directdropbackend.repository.MessageRepository;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MongoTemplate mongoTemplate;

    @Bean
    CommandLineRunner insertRooms() {
        return args -> {
            try {
                if (chatRoomRepository.count() > 0) {
                    log.info("Chat rooms already initialized. Skipping...");
                    return;
                }

                ChatRoom room1 = ChatRoom.builder()
                        .roomId(snowflakeIdGenerator.nextId())
                        .ip("2001:db8:85a3::8a2e:370:7334") // IPv6 Address
                        .users(new HashSet<>()) // Empty user set initially
                        .build();

                ChatRoom room2 = ChatRoom.builder()
                        .roomId(snowflakeIdGenerator.nextId())
                        .ip("2001:db8:85a3::8a2e:370:abcd") // Another IPv6 Address
                        .users(new HashSet<>())
                        .build();

                chatRoomRepository.saveAll(Set.of(room1, room2));
                log.info("Chat rooms initialized successfully.");
            } catch (Exception e) {
                log.error("Error inserting chat rooms: " + e.getMessage(), e);
            }
        };
    }

    @Bean
    CommandLineRunner insertUsers() {
        return args -> {
            try {
                if (userRepository.count() > 0) {
                    log.info("Users already initialized. Skipping...");
                    return;
                }

                // Fetch existing rooms
                ChatRoom room1 = chatRoomRepository.findByIp("2001:db8:85a3::8a2e:370:7334")
                        .orElseThrow(() -> new RuntimeException("ChatRoom with IPv6 2001:db8:85a3::8a2e:370:7334 not found"));
                ChatRoom room2 = chatRoomRepository.findByIp("2001:db8:85a3::8a2e:370:abcd")
                        .orElseThrow(() -> new RuntimeException("ChatRoom with IPv6 2001:db8:85a3::8a2e:370:abcd not found"));

                User user1 = User.builder()
                        .userId(snowflakeIdGenerator.nextId())
                        .username("john_doe")
                        .chatRoom(room1)
                        .build();

                User user2 = User.builder()
                        .userId(snowflakeIdGenerator.nextId())
                        .username("jane_smith")
                        .chatRoom(room2)
                        .build();

                User user3 = User.builder()
                        .userId(snowflakeIdGenerator.nextId())
                        .username("will_smith")
                        .chatRoom(room1)
                        .build();

                User user4 = User.builder()
                        .userId(snowflakeIdGenerator.nextId())
                        .username("spiderman")
                        .chatRoom(room2)
                        .build();

                userRepository.saveAll(Set.of(user1, user2, user3, user4));

                // Update ChatRooms with references to users
                room1.getUsers().add(user1);
                room2.getUsers().add(user2);
                room1.getUsers().add(user3);
                room2.getUsers().add(user4);
                chatRoomRepository.saveAll(Set.of(room1, room2));

                log.info("Users initialized successfully.");
            } catch (Exception e) {
                log.error("Error inserting users: " + e.getMessage(), e);
            }
        };
    }
}
