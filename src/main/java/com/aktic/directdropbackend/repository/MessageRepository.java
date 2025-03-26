package com.aktic.directdropbackend.repository;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoom.roomId': ?0 } }",
            "{ $sort: { 'createdAt': -1 } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }"
    })
    List<Message> findByChatRoom(Long roomId, int skip, int limit);

    @Aggregation(pipeline = {
            "{ $match: { 'message': { $regex: ?0, $options: 'i' } } }",
            "{ $match: { 'chatRoom.roomId': ?1 } }",
            "{ $sort: { 'createdAt': -1 } }",
            "{ $skip: ?2 }",
            "{ $limit: ?3 }"
    })
    List<Message> findByChatRoomWithKeyword(String keyword, Long roomId, int skip, int limit);


}
