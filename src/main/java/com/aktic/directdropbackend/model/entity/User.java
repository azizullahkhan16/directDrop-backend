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

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Field("user_id")
    private Long userId;

    @DBRef
    private ChatRoom chatRoom;

    @NonNull
    @Indexed(unique = true)
    private String username;

    @Field("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Field(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}
