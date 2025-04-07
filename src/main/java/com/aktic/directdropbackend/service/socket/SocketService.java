package com.aktic.directdropbackend.service.socket;

import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import com.aktic.directdropbackend.model.response.NotifyMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void joinChat(NotifyMessageResponse notifyMessageResponse) {
        log.info(notifyMessageResponse.getUser().getUsername() + " joined the chat room: " + notifyMessageResponse.getChatRoom().getRoomId());
        messagingTemplate.convertAndSend("/topic/chatroom." + notifyMessageResponse.getChatRoom().getRoomId(), notifyMessageResponse);
    }

    public void leaveChat(NotifyMessageResponse notifyMessageResponse) {
        messagingTemplate.convertAndSend("/topic/chatroom." + notifyMessageResponse.getChatRoom().getRoomId(), notifyMessageResponse);
    }

    public void sendMessageToChatRoom(MessageInfoResponse message) {
        // Send message to all subscribers of this chat room
        messagingTemplate.convertAndSend("/topic/chatroom." + message.getChatRoom().getRoomId(), message);
    }
}
