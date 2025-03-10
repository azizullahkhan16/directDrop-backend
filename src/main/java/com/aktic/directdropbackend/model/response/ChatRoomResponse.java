package com.aktic.directdropbackend.model.response;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long roomId;
    private String ip;
    private Instant createdAt;
    private Instant updatedAt;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.ip = chatRoom.getIp();
        this.createdAt = chatRoom.getCreatedAt();
        this.updatedAt = chatRoom.getUpdatedAt();
    }
}
