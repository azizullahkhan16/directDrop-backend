package com.aktic.directdropbackend.model.response;

import com.aktic.directdropbackend.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    private Long userId;
    private String username;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.isActive = user.getIsActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
