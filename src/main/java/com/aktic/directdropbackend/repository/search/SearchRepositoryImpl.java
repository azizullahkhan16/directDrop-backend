package com.aktic.directdropbackend.repository.search;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.Message;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<MessageInfoResponse> fullTextSearchIncludingChatRoom(ChatRoom chatRoom, String keyword, String username, Pageable pageable) {
        // Build base query for messages in the same chat room
        Query query = new Query();

        if(chatRoom != null) {
            query.addCriteria(Criteria.where("chatRoom").is(chatRoom));
        }

        // Add keyword search if provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.addCriteria(Criteria.where("message").regex(keyword, "i"));
        }

        if (username != null && !username.trim().isEmpty()) {
            Query userQuery = new Query()
                    .addCriteria(Criteria.where("username").regex(username, "i"))
                    .addCriteria(Criteria.where("chatRoom").is(chatRoom));
            List<User> matchingUsers = mongoTemplate.find(userQuery, User.class);

            if (matchingUsers.isEmpty()) {
                throw new NoSuchElementException("No users found matching username '" + username + "' in this chat room");
            }

            query.addCriteria(Criteria.where("sender").in(matchingUsers));
        }

        // Get total count first (without pagination)
        long total = mongoTemplate.count(query, Message.class);

        // Apply pagination to the query
        query.with(pageable);

        // Get messages with pagination
        List<Message> messages = mongoTemplate.find(query, Message.class);

        // Convert messages to response
        List<MessageInfoResponse> messageInfoResponses = messages.stream()
                .map(MessageInfoResponse::new)
                .collect(Collectors.toList());

        return new PageImpl<>(
                messageInfoResponses,
                pageable,
                total
        );
    }

    @Override
    public Page<MessageInfoResponse> fullTextSearchExcludingChatRoom(User user, String keyword, String username, Pageable pageable) {
        // Build base query for messages where chatRoom is null
        Query query = new Query()
                .addCriteria(Criteria.where("chatRoom").is(null)); // Chat room must be null

        // Combine all user-related criteria in a single Criteria object
        Criteria combinedCriteria = new Criteria();

        // Base condition: user must be sender or receiver
        Criteria userCriteria = new Criteria().orOperator(
                Criteria.where("sender").is(user),
                Criteria.where("receivers").in(user)
        );

        // Add username search if provided
        if (username != null && !username.trim().isEmpty()) {
            Query userQuery = new Query()
                    .addCriteria(Criteria.where("username").regex(username, "i"));
            List<User> matchingUsers = mongoTemplate.find(userQuery, User.class);

            if (matchingUsers.isEmpty()) {
                throw new NoSuchElementException("No users found matching username '" + username + "'");
            }

            // Combine user criteria with username criteria
            Criteria usernameCriteria = new Criteria().orOperator(
                    Criteria.where("sender").in(matchingUsers),
                    Criteria.where("receivers").in(matchingUsers)
            );

            // Combine both conditions with AND
            combinedCriteria.andOperator(userCriteria, usernameCriteria);
        } else {
            // If no username search, just use the user criteria
            combinedCriteria.andOperator(userCriteria);
        }

        // Add the combined criteria to the query
        query.addCriteria(combinedCriteria);

        // Add keyword search if provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.addCriteria(Criteria.where("message").regex(keyword, "i"));
        }

        // Get total count first (without pagination)
        long total = mongoTemplate.count(query, Message.class);

        // Apply pagination with skip and limit
        query.with(pageable);

        // Get messages with pagination
        List<Message> messages = mongoTemplate.find(query, Message.class);

        // Convert messages to response
        List<MessageInfoResponse> messageInfoResponses = messages.stream()
                .map(MessageInfoResponse::new)
                .collect(Collectors.toList());

        return new PageImpl<>(
                messageInfoResponses,
                pageable,
                total
        );
    }
}
