package com.email.SmartCraft.Controller;

import com.email.SmartCraft.Model.EmailRequest;
import com.email.SmartCraft.service.emailGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for email generation operations
 * Provides endpoints for generating email replies using AI
 */
@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class EmailGeneratorController {

    private final emailGeneratorService emailGeneratorService;

    /**
     * Unified endpoint for email generation
     * Handles both regular and alternate generation based on variant flag
     * 
     * @param emailRequest Request containing email content, tone, and preferences
     * @return ResponseEntity with generated email content as plain text
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
        try {
            log.info("Generating email - Variant: {}, Tone: {}",
                    emailRequest.isVariant(), emailRequest.getTone());

            // Validate request
            if (emailRequest.getEmailContent() == null || emailRequest.getEmailContent().trim().isEmpty()) {
                log.warn("Email generation failed: Empty email content");
                return ResponseEntity
                        .badRequest()
                        .body("Error: Email content cannot be empty");
            }

            // Generate email based on variant flag
            String generatedEmail;
            if (emailRequest.isVariant()) {
                generatedEmail = emailGeneratorService.generateAlternateEmailReply(emailRequest);
            } else {
                generatedEmail = emailGeneratorService.generateEmailReply(emailRequest);
            }

            log.info("Email generated successfully");
            return ResponseEntity.ok(generatedEmail);

        } catch (Exception e) {
            log.error("Error generating email: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Failed to generate email - " + e.getMessage());
        }
    }

    /**
     * Generate email with custom user query/instruction
     * Allows users to provide specific instructions for email generation
     * 
     * @param emailRequest Request containing email content and user query
     * @return ResponseEntity with generated email content as plain text
     */
    @PostMapping("/generate-with-query")
    public ResponseEntity<String> generateEmailWithQuery(@RequestBody EmailRequest emailRequest) {
        try {
            log.info("Generating email with user query: {}", emailRequest.getUserQuery());

            // Validate request
            if (emailRequest.getEmailContent() == null || emailRequest.getEmailContent().trim().isEmpty()) {
                log.warn("Email generation failed: Empty email content");
                return ResponseEntity
                        .badRequest()
                        .body("Error: Email content cannot be empty");
            }

            if (emailRequest.getUserQuery() == null || emailRequest.getUserQuery().trim().isEmpty()) {
                log.warn("Email generation failed: Empty user query");
                return ResponseEntity
                        .badRequest()
                        .body("Error: User query cannot be empty");
            }

            String generatedEmail = emailGeneratorService.generateEmailWithQuery(emailRequest);

            log.info("Email generated successfully with user query");
            return ResponseEntity.ok(generatedEmail);

        } catch (Exception e) {
            log.error("Error generating email with query: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Failed to generate email - " + e.getMessage());
        }
    }

    /**
     * Legacy endpoint for backward compatibility
     * 
     * @deprecated Use /generate with variant flag instead
     */
    @Deprecated
    @PostMapping("/generate-alternate")
    public ResponseEntity<String> generateAlternateEmail(@RequestBody EmailRequest emailRequest) {
        log.warn("Using deprecated endpoint /generate-alternate");
        String alternateReply = emailGeneratorService.generateAlternateEmailReply(emailRequest);
        return ResponseEntity.ok(alternateReply);
    }
}