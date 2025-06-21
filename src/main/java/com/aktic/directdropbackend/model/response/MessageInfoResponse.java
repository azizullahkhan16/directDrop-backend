package com.aktic.directdropbackend.model.response;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageInfoResponse {
    private String messageId;
    private UserInfoResponse sender;
    private UserInfoResponse[] receivers;
    private ChatRoomResponse chatRoom;
    private String message;
    private String attachments;
    private Instant createdAt;
    private Instant updatedAt;

    public MessageInfoResponse(Message message) {
        this.messageId = message.getMessageId().toString();
        this.sender = new UserInfoResponse(message.getSender());
        this.receivers = message.getReceivers().stream().map(UserInfoResponse::new).toArray(UserInfoResponse[]::new);
        this.chatRoom =  message.getChatRoom() == null ? null : new ChatRoomResponse(message.getChatRoom());
        this.message = message.getMessage();
        this.attachments = message.getAttachments() == null ? null : message.getAttachments().toString();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }
}
