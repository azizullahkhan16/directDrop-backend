package com.aktic.directdropbackend.model.response;

import com.aktic.directdropbackend.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifyMessageResponse {
    private UserInfoResponse user;
    private ChatRoomResponse chatRoom;
    private String message;
    private NotificationType type;
    private Instant createdAt;

}
