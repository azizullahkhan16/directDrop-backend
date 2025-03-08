package com.aktic.directdropbackend.model.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    @NonNull
    @Indexed(unique = true)
    @Field("message_id")
    private Long messageId;

    @NonNull
    @DBRef
    @Indexed
    private User sender;

    @NonNull
    @DBRef
    private List<User> receivers;

    @NonNull
    @DBRef
    private ChatRoom chatRoom;

    private String message;

    private List<String> attachments;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;

}
