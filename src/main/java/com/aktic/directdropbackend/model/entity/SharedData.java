package com.aktic.directdropbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedData {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("senderIP")
    private String senderIP;

    @JsonProperty("receiverIP")
    private String[] receiverIP;

    @JsonProperty("content")
    private String content;

    @JsonProperty("files")
    private String[] files;

    @JsonProperty("PIN")
    private Integer PIN;

    @JsonProperty("isAccessed")
    private Boolean isAccessed;

    @JsonProperty("expiresAt")
    private Instant expiresAt;

    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonProperty("updatedAt")
    private Instant updatedAt;
}
