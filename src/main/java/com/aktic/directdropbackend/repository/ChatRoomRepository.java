package com.aktic.directdropbackend.repository;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(Long roomId);
}
