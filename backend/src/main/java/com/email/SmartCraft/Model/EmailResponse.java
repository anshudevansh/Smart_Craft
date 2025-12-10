package com.email.SmartCraft.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Structured response model for email generation API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailResponse {
    private boolean success;
    private String emailContent;
    private String errorMessage;
    private ResponseMetadata metadata;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseMetadata {
        private String tone;
        private boolean variant;
        private String lengthPreference;
        private LocalDateTime timestamp;
        private String userQuery;
    }

    /**
     * Creates a successful response
     */
    public static EmailResponse success(String emailContent, EmailRequest request) {
        return EmailResponse.builder()
                .success(true)
                .emailContent(emailContent)
                .metadata(ResponseMetadata.builder()
                        .tone(request.getTone())
                        .variant(request.isVariant())
                        .lengthPreference(request.getLengthPreference())
                        .timestamp(LocalDateTime.now())
                        .userQuery(request.getUserQuery())
                        .build())
                .build();
    }

    /**
     * Creates an error response
     */
    public static EmailResponse error(String errorMessage) {
        return EmailResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .metadata(ResponseMetadata.builder()
                        .timestamp(LocalDateTime.now())
                        .build())
                .build();
    }
}
