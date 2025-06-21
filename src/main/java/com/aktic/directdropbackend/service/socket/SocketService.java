package com.aktic.directdropbackend.service.socket;

import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import com.aktic.directdropbackend.model.response.NotifyMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void joinChat(NotifyMessageResponse notifyMessageResponse) {
        log.info(notifyMessageResponse.getUser().getUsername() + " joined the chat room: " + notifyMessageResponse.getChatRoom().getRoomId());
        messagingTemplate.convertAndSend("/topic/notify." + notifyMessageResponse.getChatRoom().getRoomId(), notifyMessageResponse);
    }

    public void leaveChat(NotifyMessageResponse notifyMessageResponse) {
        messagingTemplate.convertAndSend("/topic/notify." + notifyMessageResponse.getChatRoom().getRoomId(), notifyMessageResponse);
    }

    public void sendMessageSameNetwork(MessageInfoResponse message) {
        log.info("Sending message to chat room: " + message.getChatRoom().getRoomId());
        messagingTemplate.convertAndSend("/topic/chatroom." + message.getChatRoom().getRoomId(), message);
    }

    public void sendMessageSameNetByReceiver(MessageInfoResponse message) {
        for(int i = 0; i < message.getReceivers().length; i++) {
            log.info("Sending message to user: " + message.getReceivers()[i].getUserId());
            messagingTemplate.convertAndSend("/topic/chatroom." + message.getChatRoom().getRoomId() + "." + message.getReceivers()[i].getUserId(), message);
        }
    }

    public void sendMessageAcrossNetwork(MessageInfoResponse message) {
        log.info("Sending message to user: " + message.getReceivers()[0].getUserId());
        messagingTemplate.convertAndSend("/topic/private." + message.getReceivers()[0].getUserId(), message);
    }
}
