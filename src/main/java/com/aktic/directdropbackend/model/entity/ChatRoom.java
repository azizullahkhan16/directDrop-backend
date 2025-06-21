package com.aktic.directdropbackend.model.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "chat_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @Field("room_id")
    private Long roomId;

    @Indexed(unique = true)
    private String ip;

    @DBRef
    private Set<User> users = new HashSet<>();

    @CreatedDate
    @Field("created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}
