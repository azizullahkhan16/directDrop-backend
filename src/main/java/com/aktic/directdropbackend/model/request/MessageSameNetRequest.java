package com.aktic.directdropbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSameNetRequest {
    private Long userId;
    private List<Long> receiverIds;
    private String message;
    private List<String> attachments;
    private Long chatRoomId;
}
