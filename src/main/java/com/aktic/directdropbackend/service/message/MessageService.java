package com.aktic.directdropbackend.service.message;

import com.aktic.directdropbackend.model.entity.ChatRoom;
import com.aktic.directdropbackend.model.entity.Message;
import com.aktic.directdropbackend.model.entity.User;
import com.aktic.directdropbackend.model.request.MessageAcrossNetRequest;
import com.aktic.directdropbackend.model.request.MessageSameNetRequest;
import com.aktic.directdropbackend.model.response.MessageInfoResponse;
import com.aktic.directdropbackend.repository.ChatRoomRepository;
import com.aktic.directdropbackend.repository.MessageRepository;
import com.aktic.directdropbackend.repository.UserRepository;
import com.aktic.directdropbackend.repository.search.SearchRepository;
import com.aktic.directdropbackend.service.fileStorage.FileStorageService;
import com.aktic.directdropbackend.util.ApiResponse;
import com.aktic.directdropbackend.util.SnowflakeIdGenerator;
import jakarta.security.auth.message.MessageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final SnowflakeIdGenerator idGenerator;

    public ResponseEntity<ApiResponse<MessageInfoResponse>> sendMessageSameNetwork(MessageSameNetRequest messageSameNetRequest) {
        try {
            // Verify if sender exists
            User sender = userRepository.findByUserId(messageSameNetRequest.getSenderId())
                    .orElseThrow(() -> new NoSuchElementException("Sender not found"));

            // Verify if sender belongs to a chat room
            ChatRoom chatRoom = sender.getChatRoom();
            if (chatRoom == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Sender is not part of any chat room", null));
            }

            // Get the list of users in the sender's chat room
            Set<User> chatRoomMembers = chatRoom.getUsers();

            // Validate that all receivers are in the same chat room
            List<User> receivers = messageSameNetRequest.getReceiverIds().stream()
                    .map(receiverId -> chatRoomMembers.stream()
                            .filter(user -> user.getUserId().equals(receiverId))
                            .findFirst()
                            .orElseThrow(() -> new NoSuchElementException("Receiver with ID " + receiverId + " is not in the chat room")))
                    .collect(Collectors.toList());

            // Handle attachments (if any)
            MultipartFile[] attachments = messageSameNetRequest.getAttachments();
            List<String> attachmentUrls = null;
            if (attachments != null && attachments.length > 0) {
                attachmentUrls = List.of(attachments).stream()
                        .map(fileStorageService::save)
                        .collect(Collectors.toList());
            }

            // Validation: Message and attachments cannot both be null
            if ((messageSameNetRequest.getMessage() == null || messageSameNetRequest.getMessage().trim().isEmpty())
                    && (attachmentUrls == null || attachmentUrls.isEmpty())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Message content or at least one attachment is required", null));
            }

            // Save message
            Message message = Message.builder()
                    .messageId(idGenerator.nextId())
                    .sender(sender)
                    .receivers(receivers)
                    .chatRoom(chatRoom)
                    .message(messageSameNetRequest.getMessage())
                    .attachments(attachmentUrls)
                    .build();

            Message savedMessage = messageRepository.save(message);

            return ResponseEntity.ok(new ApiResponse<>(true, "Message sent successfully", new MessageInfoResponse(savedMessage)));

        } catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));

        } catch (Exception e) {
            log.error("Unexpected error occurred while sending message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<ApiResponse<MessageInfoResponse>> sendMessageAcrossNetwork(@Valid MessageAcrossNetRequest messageAcrossNetRequest) {
        try {
            // Verify if sender exists
            User sender = userRepository.findByUserId(messageAcrossNetRequest.getSenderId())
                    .orElseThrow(() -> new NoSuchElementException("Sender not found"));

            // Verify if receiver exists
            User receiver = userRepository.findByUserId(messageAcrossNetRequest.getReceiverId())
                    .orElseThrow(() -> new NoSuchElementException("Receiver not found"));

            // Validate that sender and receiver are not same
            if(sender.getUserId().equals(receiver.getUserId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Sender and receiver cannot be the same", null));
            }

            if(sender.getChatRoom().getRoomId().equals(receiver.getChatRoom().getRoomId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Sender and receiver are in the same chat room", null));
            }

            // Handle attachments (if any)
            MultipartFile[] attachments = messageAcrossNetRequest.getAttachments();
            List<String> attachmentUrls = null;
            if (attachments != null && attachments.length > 0) {
                attachmentUrls = List.of(attachments).stream()
                        .map(fileStorageService::save)
                        .collect(Collectors.toList());
            }

            // Validation: Message and attachments cannot both be null
            if ((messageAcrossNetRequest.getMessage() == null || messageAcrossNetRequest.getMessage().trim().isEmpty())
                    && (attachmentUrls == null || attachmentUrls.isEmpty())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Message content or at least one attachment is required", null));
            }

            // Save message
            Message message = Message.builder()
                    .messageId(idGenerator.nextId())
                    .sender(sender)
                    .receivers(Arrays.asList(receiver))
                    .message(messageAcrossNetRequest.getMessage().trim())
                    .attachments(attachmentUrls)
                    .build();

            Message savedMessage = messageRepository.save(message);

            return ResponseEntity.ok(new ApiResponse<>(true, "Message sent successfully", new MessageInfoResponse(savedMessage)));

        } catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));

        } catch (Exception e) {
            log.error("Unexpected error occurred while sending message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<ApiResponse<List<MessageInfoResponse>>> getMessagesSameNetwork(
            Long userId, Integer pageNumber, Integer limit, String keyword) {
        try {
            // Fetch user
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            // Get chat room of user
            ChatRoom chatRoom = user.getChatRoom();
            if (chatRoom == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "User is not in a chat room", null));
            }

            // Pagination setup
            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 50;

            List<Message> messages;

            if(keyword != null && !keyword.trim().isEmpty()) {
                messages = messageRepository.findByChatRoomWithKeyword(keyword, chatRoom.getRoomId(), page * size, size);
            } else {
                messages = messageRepository.findByChatRoom(chatRoom.getRoomId(), page * size, size);
            }

            // convert messages to response
            List<MessageInfoResponse> messageInfoResponses = messages.stream()
                    .map(MessageInfoResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(true, "Messages retrieved successfully", messageInfoResponses));

        } catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error occurred while getting messages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
