package com.aktic.directdropbackend.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSameNetRequest {

    @NotNull(message = "Sender ID cannot be null.")
    private Long senderId;

    private List<Long> receiverIds;

    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters.")
    private String message;

    private MultipartFile[] attachments;

}
