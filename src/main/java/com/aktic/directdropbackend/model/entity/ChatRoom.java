package com.aktic.directdropbackend.model.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "chat_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    @NonNull
    @Indexed(unique = true)
    @Field("room_id")
    private Long roomId;

    @NonNull
    @Indexed(unique = true)
    private String ip;

    @DBRef
    private Set<User> users = new HashSet<>();

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}
